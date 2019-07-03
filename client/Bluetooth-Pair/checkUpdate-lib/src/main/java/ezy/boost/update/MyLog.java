package ezy.boost.update;

import android.util.Log;

import com.orhanobut.logger.Logger;

public class MyLog {
    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }
    public static void v(String tag, String msg, Throwable throwable) {
        Log.v(tag, msg, throwable);
    }
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
        Logger.d(msg);
    }
    public static void d(String tag, String msg, Throwable throwable) {
        Log.d(tag, msg, throwable);
        Logger.d(msg, throwable);
    }
    public static void i(String tag, String msg) {
        Log.i(tag, msg);
        Logger.i(msg);
    }
    public static void i(String tag, String msg, Throwable throwable) {
        Log.i(tag, msg, throwable);
        Logger.i(msg, throwable);
    }
    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }
    public static void w(String tag, String msg, Throwable throwable) {
        Log.w(tag, msg, throwable);
    }
    public static void e(String tag, String msg) {
        Log.e(tag, msg);
        Logger.e(msg);
    }
    public static void e(String tag, String msg, Throwable throwable) {
        Log.e(tag, msg, throwable);
        Logger.e(msg, throwable);
    }
    public static void wtf(String tag, String msg) {
        Log.wtf(tag, msg);
    }
    public static void wtf(String tag, String msg, Throwable throwable) {
        Log.wtf(tag, msg, throwable);
    }
}
