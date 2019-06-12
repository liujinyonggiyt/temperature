package ljy.view.activity;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.bluetooth.R;
import ljy.utils.MyLog;
import ljy.utils.ToastUtil;
import ljy.widget.TitleBar;

public class MainActivity extends BaseActivity {
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.buttion_go_blue)
    Button goBluetoothMenu;
    @BindView(R.id.buttion_go_socket)
    Button goSocketMenu;
    @BindView(R.id.buttion_go_qrcode)
    Button goQrcodeMenu;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            titlebar.setTitle(R.string.app_name);
            titlebar.setBackgroundResource(R.color.blue);
            titlebar.setImmersive(true);
            titlebar.setTitleColor(Color.WHITE);

            calendar.set(2019,6, 15, 0, 0);//20190715
            MyLog.e(Tag, "过期时间："+calendar.getTime());
            if(System.currentTimeMillis()>=calendar.getTimeInMillis()){//过期
                showOutdateDialog();
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }

    private void showOutdateDialog(){
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);//图标
        normalDialog.setTitle("注意");
        normalDialog.setMessage("产品过期了，请联系开发人员！");
        normalDialog.setCancelable(false);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(-1);
                    }
                });
        // 显示
        normalDialog.show();
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
    @OnClick({R.id.buttion_go_blue, R.id.buttion_go_socket, R.id.buttion_go_qrcode, R.id.buttion_go_bind, R.id.buttion_go_look_speed})
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
                case R.id.buttion_go_bind:{
                    Intent intent = new Intent(MainActivity.this, BindActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.buttion_go_qrcode:{
                    Intent intent = new Intent(MainActivity.this, QrCodeActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.buttion_go_look_speed:{
                    Intent intent = new Intent(MainActivity.this, SpeedDataListActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }
}
