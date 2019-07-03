package ljy.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.boost.update.ICheckAgent;
import ezy.boost.update.IUpdateChecker;
import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateInfo;
import ezy.boost.update.UpdateManager;
import ezy.boost.update.UpdateUtil;
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
    @BindView(R.id.buttion_go_qrcode)
    Button goQrcodeMenu;
//    private final Calendar calendar = Calendar.getInstance();


    String mCheckUrl = "http://140.143.156.15:10002/checkUpdate";
//    String mCheckUrl = "http://192.168.251.76:10002/checkUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            String title = getResources().getString(R.string.app_name)+"-v"+UpdateUtil.getVersionCode(this);
            titlebar.setTitle(title);
            titlebar.setBackgroundResource(R.color.blue);
            titlebar.setImmersive(true);
            titlebar.setTitleColor(Color.WHITE);

//            calendar.set(2019,6, 15, 0, 0);//20190815
//            MyLog.e(Tag, "过期时间："+calendar.getTime());
//            if(System.currentTimeMillis()>=calendar.getTimeInMillis()){//过期
//                showOutdateDialog();
//            }


            //检查版本更新
            {
                UpdateManager.setDebuggable(true);
                UpdateManager.setWifiOnly(false);
                UpdateManager.setUrl(mCheckUrl, "yyb");
                UpdateManager.check(this);
            }

        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }



    void checkUpdateAgent(boolean isManual, final boolean hasUpdate, final boolean isForce, final boolean isSilent, final boolean isIgnorable, final int
            notifyId) {
        UpdateManager.create(this).setChecker(new IUpdateChecker() {
            @Override
            public void check(ICheckAgent agent, String url) {
                MyLog.i("ezy.update", "checking");
                agent.setInfo("");
            }
        }).setManual(isManual).setNotifyId(notifyId).setParser(new IUpdateParser() {
            @Override
            public UpdateInfo parse(String source) throws Exception {
                UpdateInfo info = new UpdateInfo();
                info.hasUpdate = hasUpdate;
                info.updateContent = "• 支持文字、贴纸、背景音乐，尽情展现欢乐气氛；\n• 两人视频通话支持实时滤镜，丰富滤镜，多彩心情；\n• 图片编辑新增艺术滤镜，一键打造文艺画风；\n• 资料卡新增点赞排行榜，看好友里谁是魅力之王。";
                info.versionName = "v5.8.7";
                info.url = "http://140.143.156.15/app-release.apk";
                info.md5 = "56cf48f10e4cf6043fbf53bbbc4009a3";
                info.size = 10149314;
                info.isForce = isForce;
                info.isIgnorable = isIgnorable;
                info.isSilent = isSilent;
                return info;
            }
        }).check();
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
    @OnClick({R.id.buttion_go_blue, R.id.buttion_go_socket, R.id.buttion_go_qrcode, R.id.buttion_go_bind, R.id.buttion_go_look_speed, R.id.buttion_go_cal})
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
                case R.id.buttion_go_cal:{//计算
                    Intent intent = new Intent(MainActivity.this, CalculateActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }
}
