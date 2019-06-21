package ljy.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ljy.base.bean.SpeedDataBus;
import ljy.base.bean.SpeedDataSelectBus;
import ljy.base.constant.SpeedDataContant;
import ljy.bluetooth.R;
import ljy.mapping.SpeedData;
import ljy.misc.SpeedDataItemAdapter;
import ljy.misc.SpeedDataItemSelectAdapter;
import ljy.mrg.SqliteMrg;
import ljy.utils.MyLog;
import ljy.utils.ToastUtil;
import ljy.view.activity.SpeedDataListActivity;
import ljy.widget.TitleBar;

public class CreateSpeedDataSelectDialog extends Dialog {
    private static final String TAG = CreateSpeedDataSelectDialog.class.getSimpleName();
    /**
     * 上下文对象 *
     */
    Activity context;
    private final int rowIndex;
    SpeedDataItemSelectAdapter adapter;
    private List<SpeedData> speedDataList = new ArrayList<>();
    private Button btn_cancel;

    public CreateSpeedDataSelectDialog(Activity context, int rowIndex) {
        super(context, 0);
        this.context = context;
        this.rowIndex = rowIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.create_speed_data_select_dialog);

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

        TitleBar titlebar = findViewById(R.id.titlebar);
        titlebar.setTitle("选择一项纪录");
        titlebar.setBackgroundResource(R.color.blue);
        titlebar.setImmersive(true);
        titlebar.setTitleColor(Color.WHITE);

        speedDataList.addAll(SqliteMrg.getInstance().findAll(SpeedData.class));
        ListView listView = findViewById(R.id.speed_data_select_listView);
        adapter = new SpeedDataItemSelectAdapter(context, R.layout.speed_list_select_item, speedDataList);
        listView.setAdapter(adapter);
        // 根据id在布局中找到控件对象
        // 为按钮绑定点击事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SpeedData speedData = speedDataList.get(position);

                EventBus.getDefault().post(new SpeedDataSelectBus(SpeedDataContant.ON_SELECT_SPEED_DATA, speedData, rowIndex, position+1));
                CreateSpeedDataSelectDialog.this.cancel();
            }
        });
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSpeedDataSelectDialog.this.cancel();
            }
        });
        this.setCancelable(true);
    }

}
