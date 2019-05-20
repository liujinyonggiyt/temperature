package ljy.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ljy.ProtoEnum;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import ljy.base.activity.BaseActivity;
import ljy.base.bean.SocketMessageBean;
import ljy.bluetooth.R;
import ljy.msg.ByteStringRequest;
import ljy.msg.RequestMsg;
import ljy.msg.ServerResponse;
import ljy.net.AbsConnectServer;
import ljy.net.NettyConnectServer;
import ljy.utils.MyLog;
import ljy.widget.TitleBar;

import static ljy.base.bean.SocketMessageBean.ON_RECEIVE_MSG;
import static ljy.base.bean.SocketMessageBean.SOCKET_CONNECTED;
import static ljy.base.bean.SocketMessageBean.SOCKET_DISCONNECTED;

public class BindActivity extends BaseActivity {
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

    @BindView(R.id.socket_bind_ip)
    EditText socket_bind_ipText;
    @BindView(R.id.buttion_bind)
    Button bindButton;
    @BindView(R.id.bindStat)
    TextView bindStat;


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
        titlebar.setTitle("设备绑定");
        titlebar.setBackgroundResource(R.color.blue);
        titlebar.setImmersive(true);
        titlebar.setTitleColor(Color.WHITE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind;
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
    @OnClick({R.id.buttion_connet_server, R.id.buttion_bind})
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
                case R.id.buttion_bind:{//绑定设备
                    if(!connectServer.isActive()){
                        Toast.makeText(BindActivity.this, "请先连接服务器！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String msg = "bind:"+socket_bind_ipText.getText().toString()+"\r\n";
                    ByteBuf serverResponse = Unpooled.buffer(msg.length());
                    serverResponse.writeBytes(msg.getBytes());
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
                //隐藏
                ip_editText.setVisibility(View.GONE);
                port_editText.setVisibility(View.GONE);
                connect.setVisibility(View.GONE);
                break;
            case SocketMessageBean.SOCKET_DISCONNECTED:
                connectState.setText("服务器连接状态:未连接");
                ip_editText.setVisibility(View.VISIBLE);
                port_editText.setVisibility(View.VISIBLE);
                connect.setVisibility(View.VISIBLE);

                socket_bind_ipText.setVisibility(View.VISIBLE);
                bindButton.setVisibility(View.VISIBLE);
                break;
            case SocketMessageBean.ON_RECEIVE_MSG:{
                ByteStringRequest requestMsg = (ByteStringRequest) socketMessageBean.getMsg();
//                System.out.println(requestMsg.getString());
//                ProtoEnum protoEnum = ProtoEnum.values()[requestMsg.getMsgCode()];
//                switch (protoEnum){
//                    case S_SEND_STRING:{
//                        String msg = requestMsg.getString();
//                        msgReceive.setText(msg+"M/s");
//                        break;
//                    }
//                    default:break;
//                }

                String msg = new String(requestMsg.getBytes(), "UTF-8");

                if(msg.startsWith("bindSuccess:")){//绑定设备成功
                    bindStat.setText(msg.replace("bindSuccess:", ""));
                    //隐藏
                    socket_bind_ipText.setVisibility(View.GONE);
                    bindButton.setVisibility(View.GONE);
                }else if(msg.startsWith("unbind:")){
                    bindStat.setText("None");
                    //隐藏
                    socket_bind_ipText.setVisibility(View.VISIBLE);
                    bindButton.setVisibility(View.VISIBLE);
                }else{
                    msgReceive.setText(msg+"M/s");
                }

            }
            default:
                break;
        }
    }
}
