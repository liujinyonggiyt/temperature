package ljy.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.base.bean.BlueMessageBean;
import ljy.base.constant.BltContant;
import ljy.bluetooth.R;
import ljy.mapping.SpeedData;
import ljy.mrg.SqliteMrg;
import ljy.mrg.SystemTimeMrg;
import ljy.service.ReceiveSocketService;
import ljy.service.SendSocketService;
import ljy.utils.MyLog;
import ljy.utils.ToastUtil;
import ljy.utils.factory.ThreadPoolProxyFactory;
import ljy.view.dialog.CreateSpeedDataDialog;
import ljy.widget.TitleBar;

public class BluetoothTongxunActivity extends BaseActivity {

    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.content_ly)
    LinearLayout contentLy;
    @BindView(R.id.go_edit_text)
    EditText goEditText;
    @BindView(R.id.go_text_btn)
    Button goTextBtn;
    @BindView(R.id.go_file_btn)
    Button goFileBtn;
    @BindView(R.id.text)
    TextView text;

    /**
     * 存储数据
     */
    @BindView(R.id.btn_save)
    Button saveBtn;
    /**
     * 查看数据
     */
    @BindView(R.id.btn_look)
    Button lookBtn;


    private final ReceiveSocketService receiveSocketService = new ReceiveSocketService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            EventBus.getDefault().register(this);
            titlebar.setTitle(getIntent().getStringExtra("devicename"));
            titlebar.setBackgroundResource(R.color.blue);
            titlebar.setImmersive(true);
            titlebar.setTitleColor(Color.WHITE);
            //开启消息接收端
            ThreadPoolProxyFactory.getNormalThreadPoolProxy().execute(new Runnable() {
                @Override
                public void run() {
                    receiveSocketService.receiveMessage();
                }
            });
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tongxun;
    }


    @OnClick({R.id.go_text_btn, R.id.go_file_btn, R.id.btn_save, R.id.btn_look})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_text_btn:
                //发送文字消息
                if (TextUtils.isEmpty(goEditText.getText().toString())) {
                    ToastUtil.shortShow("请先输入信息");
                } else {
                    SendSocketService.sendMessage(goEditText.getText().toString());
                }
                break;
            case R.id.go_file_btn:
                SendSocketService.sendMessageByFile(Environment.getExternalStorageDirectory()+"/test.png");
                break;
            case R.id.btn_save:{//存储数据
                String speedStr = text.getText().toString();
                float speed = 0F;
                if(!speedStr.isEmpty()){
                    speed = Float.parseFloat(speedStr);
                }
                SpeedData speedData = new SpeedData(2, speed, SystemTimeMrg.getInstance().getCurTime());
                int order = SqliteMrg.getInstance().getNextSpeedDataId();
                CreateSpeedDataDialog createSpeedDataDialog = new CreateSpeedDataDialog(BluetoothTongxunActivity.this, order, speedData);
                createSpeedDataDialog.show();
                break;
            }
            case R.id.btn_look:{//查看数据
                Intent intent = new Intent(BluetoothTongxunActivity.this, SpeedDataListActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        receiveSocketService.stop();
    }

    /**
     * RECEIVER_MESSAGE:21 收到消息
     * BltContant.SEND_TEXT_SUCCESS:发送消息成功
     *BltContant.SEND_FILE_NOTEXIT:文件不存在
     * BltContant.SEND_FILE_IS_FOLDER:不能发送文件夹
     * @param blueMessageBean
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BlueMessageBean blueMessageBean) {
        switch (blueMessageBean.getId()) {
            case ReceiveSocketService.RECEIVER_MESSAGE:
//                MyLog.d("收到消息", blueMessageBean.getContent());
//                text.append("收到消息:" + blueMessageBean.getContent() + "\n");
                text.setText("当前速度:"+blueMessageBean.getContent()+"M/s");
                break;
            case BltContant.SEND_TEXT_SUCCESS:
                text.append("我:" + goEditText.getText().toString() + "\n");
                goEditText.setText("");
                break;
            case BltContant.SEND_FILE_NOTEXIT:
                ToastUtil.shortShow("发送的文件不存在，内存根目录下的test.png");
                break;
            case BltContant.SEND_FILE_IS_FOLDER:
                ToastUtil.shortShow("不能传送一个文件夹");
                break;
            default:
                break;
        }
    }
}
