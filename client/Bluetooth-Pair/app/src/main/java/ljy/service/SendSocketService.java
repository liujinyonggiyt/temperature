package ljy.service;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import ljy.APP;
import ljy.base.bean.BlueMessageBean;
import ljy.base.constant.BltContant;
import ljy.utils.MyLog;

/**
 * 发送消息
 */
public class SendSocketService {
    private static final String TAG = SendSocketService.class.getSimpleName();

    /**
     * 发送文本消息
     *
     * @param message
     */
    public static void sendMessage(String message) {
        if (APP.bluetoothSocket == null || TextUtils.isEmpty(message)) return;
        try {
            message += "\n";
            OutputStream outputStream = APP.bluetoothSocket.getOutputStream();
            outputStream.write(message.getBytes("utf-8"));
            outputStream.flush();
            EventBus.getDefault().post(new BlueMessageBean(BltContant.SEND_TEXT_SUCCESS));
        } catch (IOException e) {
            MyLog.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 发送文件
     */
    public static void sendMessageByFile(String filePath) {
        if (APP.bluetoothSocket == null || TextUtils.isEmpty(filePath)) return;
        try {
            OutputStream outputStream = APP.bluetoothSocket.getOutputStream();
            //要传输的文件路径
            File file = new File(filePath);
            //说明不存在该文件
            if (!file.exists()){
                EventBus.getDefault().post(new BlueMessageBean(BltContant.SEND_FILE_NOTEXIT));
                return;
            }

            //说明该文件是一个文件夹
            if (file.isDirectory()) {
                EventBus.getDefault().post(new BlueMessageBean(BltContant.SEND_FILE_IS_FOLDER));
                return;
            }

            //1、发送文件信息实体类
            outputStream.write("file".getBytes("utf-8"));
            //将文件写入流
            FileInputStream fis = new FileInputStream(file);
            //每次上传1M的内容
            byte[] b = new byte[1024];
            int length;
            int fileSize = 0;//实时监测上传进度
            while ((length = fis.read(b)) != -1) {
                fileSize += length;
                MyLog.i("socketChat", "文件上传进度：" + (fileSize / file.length() * 100) + "%");
                //2、把文件写入socket输出流
                outputStream.write(b, 0, length);
            }
            //关闭文件流
            fis.close();
            //该方法无效
            //outputStream.write("\n".getBytes());
            outputStream.flush();
            EventBus.getDefault().post(new BlueMessageBean(BltContant.SEND_FILE_SUCCESS));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
