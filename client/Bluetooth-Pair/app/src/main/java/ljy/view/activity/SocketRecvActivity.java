package ljy.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.base.bean.BlueMessageBean;
import ljy.base.bean.SocketMessageBean;
import ljy.base.constant.BltContant;
import ljy.bluetooth.R;
import ljy.net.SocketConnect;
import ljy.service.ReceiveSocketService;
import ljy.utils.MyLog;
import ljy.utils.ToastUtil;
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
    Text connectState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_socket_recv;
    }

    /**
     * 监听事件
     *
     * @param view
     */
    @OnClick({R.id.buttion_connet_server})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.buttion_connet_server:{
                    String ip = ip_editText.getText().toString();
                    int port = Integer.parseInt(port_editText.getText().toString());
                    //连接服务器
                    new SocketConnect(ip, port, new SocketConnect.Callback() {
                        @Override
                        public void onConnected() {
                            EventBus.getDefault().post(new SocketMessageBean(SOCKET_CONNECTED));
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
                        public void onReceived(byte[] msg) {
                            EventBus.getDefault().post(new SocketMessageBean(ON_RECEIVE_MSG, new String(msg)));
                        }

                        @Override
                        public void onError(String msg) {

                        }
                    });
                    break;
                }
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SocketMessageBean socketMessageBean) {
        switch (socketMessageBean.getId()) {
            case SocketMessageBean.SOCKET_CONNECTED:
                connectState.setData("conneted");
                break;
            case SocketMessageBean.SOCKET_DISCONNECTED:
                connectState.setData("disconneted");
                break;
            case SocketMessageBean.ON_RECEIVE_MSG:{
                String msg = socketMessageBean.getContent();
                MyLog.i(Tag, msg);
                break;
            }
            default:
                break;
        }
    }
}
