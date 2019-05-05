package ljy.net;

import android.os.Looper;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import ljy.utils.MyLog;

public class SocketConnect {
    private static final String TAG = "SocketConnect";
    private String ip;
    private int port;
    private Callback callback;

    private Socket mSocket;
    private InputStream is = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;
    private OutputStream os = null;

    public boolean isConnected = false;
    private Thread mThread;
    private byte[] buffer = new byte[1024];


    private Thread watchThread = null;

    private Boolean isAutoConnect = true;


    private long maxcs = 5000;   //接收服务器返回心跳数据的最大超时时间，如果不需要，可设置为一个比较大的数字
    private long lasttime = 0;
    private boolean waitAnswer = false;

    public interface Callback{
        void onConnected();
        void onDisconnected();
        void onReconnected();
        void onSend();
        void onReceived(byte[] msg);
        void onError(String msg);
    }

    public SocketConnect(String ip, int port,Callback callback) {
        super();
        this.ip = ip;
        this.port = port;
        this.callback = callback;
    }

    /**
     * 设置等待接收心跳包的最大超时时间
     * @param t 毫秒
     */
    public void setMaxOvertime(long t)
    {
        maxcs = t;
    }


    /**
     * 开始连接
     */
    public void connect()
    {
        _disconnect();

        if (Thread.currentThread()==Looper.getMainLooper().getThread()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    _connect();
                }
            }).start();
        }else {
            _connect();
        }

        //连接了socket之后，才创建监听进程。
        openWatchThread();
    }



    /**
     * 断开连接
     */
    public void disconnect()
    {
        _disconnect();

        closeWatchThread();
    }





    private void _connect(){
        try {

            mSocket = new Socket(ip,port);

            boolean isConnect = mSocket.isConnected();

            if (isConnect) {

                is = mSocket.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

                os = mSocket.getOutputStream();

                isConnected = true;
                callback.onConnected();
                MyLog.i(TAG, "onConnected");

                //创建监听线程
                openThread();



            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            MyLog.e(TAG, "onError");
        }

    }





    private void _disconnect()
    {
        try {
            if (mSocket!=null) {

                isConnected = false;
                closeThread();

                if (!mSocket.isClosed()) {
                    if(!mSocket.isInputShutdown()){
                        mSocket.shutdownInput();
                    }
                    if (!mSocket.isOutputShutdown()) {
                        mSocket.shutdownOutput();
                    }
                    //此处先关socket再关流。
                    if (br!=null) {
                        br.close();
                        br=null;
                    }
                    if (isr!=null) {
                        isr.close();
                        isr=null;
                    }
                    if (is!=null) {
                        is.close();
                        is=null;
                    }
                    if (os!=null) {
                        os.close();
                        os=null;
                    }

                    mSocket.close();
                }
                mSocket = null;

                //调用回调
                callback.onDisconnected();
                MyLog.i(TAG, "onDisconnected");
            }


        } catch (Exception e) {
            // TODO: handle exception
            callback.onError("断开连接异常");
            MyLog.e(TAG, "onError");
        }

    }


    private void closeThread()
    {
        if (mThread!=null) {
            isConnected = false;
            mThread.interrupt();
            mThread = null;
            MyLog.i(TAG, "close thread");
        }
    }

    private void closeWatchThread()
    {
        if (watchThread!=null) {
            isAutoConnect = false;
            watchThread.interrupt();
            watchThread = null;
            MyLog.i(TAG, "close watchThread");
        }
    }

    private void openThread()
    {
        closeThread();
        mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isConnected) {
                    try {
                        int readLen=0;

                        readLen = is.read(buffer);
                        if (readLen>0) {
                            byte[] data = new byte[readLen];
                            System.arraycopy(buffer, 0, data, 0, readLen);

                            callback.onReceived(data);
                            MyLog.i(TAG, "onReceived"+":"+new String(data));


                            lasttime = System.currentTimeMillis();
                        }

                    } catch (Exception e) {
                        callback.onError("读取数据异常");
                        MyLog.e(TAG, "onError");
                    }

                }
            }
        });
        mThread.start();
    }

    private void openWatchThread()
    {
        //closeWatchThread();
        if (watchThread!=null) {
            return;
        }
        watchThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isAutoConnect) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    try {
                        //加入超时判断
                        if (waitAnswer) {
                            if((System.currentTimeMillis() - lasttime) > maxcs){
                                isConnected = false;
                            }
                        }

                        sendHeart(0xff);

                        if (isConnected) {

                        }else {
                            //未连接的情况下，重新连接服务器
                            MyLog.i(TAG, "onReconnect");
                            callback.onReconnected();
                            _disconnect();
                            _connect();
                        }



                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        callback.onError("读取数据异常");
                        MyLog.e(TAG, "onError");
                    }

                }
            }
        });
        watchThread.start();
    }


    /**
     * 发送命令
     * @param msg  信息
     */
    public void send(byte[] msg)
    {
        try {
            os.write(msg);
            os.flush();

            callback.onSend();
            MyLog.e(TAG, "onSend");

            if (!waitAnswer) {
                waitAnswer = true;
                lasttime = System.currentTimeMillis();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            callback.onError("发送失败");
            MyLog.e(TAG, "onError");

        }

    }


    /**
     * 发送心跳包
     * @param i
     */
    private void sendHeart(int i)
    {
        try {
            os.write(i);
            os.flush();

            if (mSocket.isInputShutdown()||mSocket.isOutputShutdown()) {
                isConnected = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            MyLog.e(TAG, "sendHeart fail");
            isConnected = false;
        }
    }

    public Boolean getAutoConnect() {
        return isAutoConnect;
    }

    public void setAutoConnect(Boolean autoConnect) {
        isAutoConnect = autoConnect;
    }
}
