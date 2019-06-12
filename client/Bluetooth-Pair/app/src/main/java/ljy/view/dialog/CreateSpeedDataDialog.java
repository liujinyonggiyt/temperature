package ljy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import ljy.base.bean.SpeedDataBus;
import ljy.base.constant.SpeedDataContant;
import ljy.bluetooth.R;
import ljy.mapping.SpeedData;
import ljy.mrg.SqliteMrg;
import ljy.utils.MyLog;
import ljy.utils.ToastUtil;

public class CreateSpeedDataDialog extends Dialog {
    private static final String TAG = CreateSpeedDataDialog.class.getSimpleName();
        /**
         * 上下文对象 *
         */
        Activity context;

        private Button btn_save;
        private Button btn_cancel;

        public TextView text_id;

        public EditText text_speed;

        public EditText text_time;
        private int order;
        private SpeedData speedData;
        public CreateSpeedDataDialog(Activity context, int order, SpeedData speedData) {
            super(context, 0);
            this.context = context;
            this.order = order;
            this.speedData = speedData;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 指定布局
            this.setContentView(R.layout.create_speed_data_dialog);

            text_id = (TextView) findViewById(R.id.text_id);
            text_speed = (EditText) findViewById(R.id.text_speed);
            text_time = (EditText) findViewById(R.id.text_time);

            text_id.setText(""+order);
            text_speed.setText(""+speedData.getSpeed());
            text_time.setText(speedData.getTime());
            /*
             * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
             * 对象,这样这可以以同样的方式改变这个Activity的属性.
             */
            Window dialogWindow = this.getWindow();

            WindowManager m = context.getWindowManager();
            Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
            p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
            dialogWindow.setAttributes(p);

            // 根据id在布局中找到控件对象
            btn_save = (Button) findViewById(R.id.btn_save);
            // 为按钮绑定点击事件监听器
            btn_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        int dbOrder = Integer.parseInt(text_id.getText().toString());
                        speedData.setSpeed(Float.parseFloat(text_speed.getText().toString()));
                        speedData.setTime(text_time.getText().toString());
                        boolean isSuccess = SqliteMrg.getInstance().save(dbOrder, speedData);
                        if(isSuccess){
                            EventBus.getDefault().post(new SpeedDataBus(SpeedDataContant.ON_SPEED_DATA_SAVE));
                            ToastUtil.shortShow("保存成功！");
                            CreateSpeedDataDialog.this.cancel();
                        }else{
                            ToastUtil.shortShow("保存失败！");
                        }
                    }catch (Exception e){
                        MyLog.e(TAG, e.getMessage());
                        ToastUtil.shortShow("输入格式错误！");
                    }
                }
            });
            btn_cancel = (Button) findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreateSpeedDataDialog.this.cancel();
                }
            });
            this.setCancelable(true);
        }

}
