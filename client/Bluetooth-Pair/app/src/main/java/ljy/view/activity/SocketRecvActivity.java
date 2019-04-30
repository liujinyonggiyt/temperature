package ljy.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ljy.base.activity.BaseActivity;
import ljy.bluetooth.R;

public class SocketRecvActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_socket_recv;
    }
}
