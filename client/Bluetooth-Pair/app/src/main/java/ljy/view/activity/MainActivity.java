package ljy.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.bluetooth.R;
import ljy.utils.MyLog;
import ljy.widget.TitleBar;

public class MainActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.buttion_go_blue)
    Button goBluetoothMenu;
    @BindView(R.id.buttion_go_socket)
    Button goSocketMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            titlebar.setTitle(R.string.app_name);
            titlebar.setBackgroundResource(R.color.blue);
            titlebar.setImmersive(true);
            titlebar.setTitleColor(Color.WHITE);
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 监听事件
     *
     * @param view
     */
    @OnClick({R.id.buttion_go_blue, R.id.buttion_go_socket})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.buttion_go_blue:{
                    Intent intent = new Intent(MainActivity.this, BluetoothListActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.buttion_go_socket:{
                    Intent intent = new Intent(MainActivity.this, SocketRecvActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }
}
