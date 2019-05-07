package ljy;

import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.readystatesoftware.notificationlog.Log;

import ljy.bluetooth.BuildConfig;
import ljy.utils.MyLog;


public class APP extends Application {
    private final String TAG = "APP";
    //不管是蓝牙连接方还是服务器方，得到socket对象后都传入
    public static volatile BluetoothSocket bluetoothSocket;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

//        if(BuildConfig.DEBUG){
            Log.initialize(context);
//        }

        Logger.addLogAdapter(new AndroidLogAdapter(){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return false;
            }
        });
        Logger.addLogAdapter(new DiskLogAdapter(){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return false;
            }
        });
        MyLog.i(TAG, "app on create!");
//        MyLog.i(TAG, Environment.getExternalStorageState());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
