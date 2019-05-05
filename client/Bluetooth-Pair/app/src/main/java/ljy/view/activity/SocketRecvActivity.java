package ljy.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.ProtoEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.base.bean.SocketMessageBean;
import ljy.bluetooth.R;
import ljy.msg.RequestMsg;
import ljy.msg.ServerResponse;
import ljy.net.AbsConnectServer;
import ljy.net.NettyConnectServer;
import ljy.utils.MyLog;
import ljy.widget.TitleBar;

import static ljy.base.bean.SocketMessageBean.ON_RECEIVE_MSG;
import static ljy.base.bean.SocketMessageBean.SOCKET_CONNECTED;
import static ljy.base.bean.SocketMessageBean.SOCKET_DISCONNECTED;

public class SocketRecvActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.socket_ip)
    EditText ip_editText;
    @BindView(R.id.socket_port)
    EditText port_editText;
    @BindView(R.id.buttion_connet_server)
    Button connect;
    @BindView(R.id.connectStat)
    TextView connectState;

    @BindView(R.id.tosend_edit_text)
    EditText tosend_edit_text;
    @BindView(R.id.text_send_btn)
    Button sendMsg;

    @BindView(R.id.serverMsg)
    TextView msgReceive;

    /**
     * 服务器连接
     */
    private AbsConnectServer connectServer = new NettyConnectServer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        titlebar.setTitle("网络通信");
        titlebar.setBackgroundResource(R.color.blue);
        titlebar.setImmersive(true);
        titlebar.setTitleColor(Color.WHITE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_socket_recv;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        connectServer.disconnect();
    }

    /**
     * 监听事件
     *
     * @param view
     */
    @OnClick({R.id.buttion_connet_server, R.id.text_send_btn})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.buttion_connet_server:{//连接服务器
                    String ip = ip_editText.getText().toString();
                    int port = Integer.parseInt(port_editText.getText().toString());

//                    if(!connectServer.isNeedReconnect(ip, port)){
//                        return;
//                    }
                    //连接服务器
                    this.connectServer.connect(ip, port, new AbsConnectServer.Callback() {
                        @Override
                        public void onConnected() {
                            EventBus.getDefault().post(new SocketMessageBean(SOCKET_CONNECTED));

                            //测试消息
                            {
                                ServerResponse serverResponse = new ServerResponse(ProtoEnum.C_TEST);

//                                connectServer.sendMsg(serverResponse);
                            }

                        }

                        @Override
                        public void onDisconnected() {
                            EventBus.getDefault().post(new SocketMessageBean(SOCKET_DISCONNECTED));
                        }

                        @Override
                        public void onReconnected() {
                            EventBus.getDefault().post(new SocketMessageBean(SOCKET_CONNECTED));
                        }

                        @Override
                        public void onSend() {

                        }

                        @Override
                        public void onReceived(RequestMsg msg) {
                            EventBus.getDefault().post(new SocketMessageBean(ON_RECEIVE_MSG, msg));
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    });
                    break;
                }
                case R.id.text_send_btn:{//发送消息
                    if(!connectServer.isActive()){
                        Toast.makeText(SocketRecvActivity.this, "请先连接服务器！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ServerResponse serverResponse = new ServerResponse(ProtoEnum.C_SEND_STRING);
                    serverResponse.writeUTF(tosend_edit_text.getText().toString());
                    connectServer.sendMsg(serverResponse);
                    break;
                }
                default:break;
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketMessageBean socketMessageBean) throws IOException {
        switch (socketMessageBean.getId()) {
            case SocketMessageBean.SOCKET_CONNECTED:
                connectState.setText("服务器连接状态:已连接");
                break;
            case SocketMessageBean.SOCKET_DISCONNECTED:
                connectState.setText("服务器连接状态:未连接");
                break;
            case SocketMessageBean.ON_RECEIVE_MSG:{
                RequestMsg requestMsg = socketMessageBean.getMsg();
                ProtoEnum protoEnum = ProtoEnum.values()[requestMsg.getMsgCode()];
                switch (protoEnum){
                    case S_SEND_STRING:{
                        String msg = requestMsg.getString();
                        msgReceive.setText(msg+"M/s");
                        break;
                    }
                    default:break;
                }
            }
            default:
                break;
        }
    }
}
