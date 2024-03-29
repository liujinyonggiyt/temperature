package ljy.service;

import android.os.Environment;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import ljy.APP;
import ljy.base.bean.BlueMessageBean;
import ljy.utils.MyLog;

/**
 * 接收消息服务端
 *
 */
public class ReceiveSocketService {
    public static final String TAG = "ReceiveSocketService";
    public static final int RECEIVER_MESSAGE =  21;//收到消息
    public static final int RECEIVER_FILE =  22;//收到文件

    private static final byte BEGIN_BIT = 0;//起始位
    private static final byte END_BIT = 1-BEGIN_BIT;//停止位
    private State state = new WaitDataState();
    private volatile boolean running = true;
    /**
     * @param num:要获取二进制值的数
     * @param index:倒数第一位为0，依次类推
     */
    public static int get(int num, int index){
        return (num & (0x1 << index)) >> index;
    }

    public static int combineBit(byte[] bits){
        int result = 0x0;
        for(int i=0;i<bits.length;++i){
            result = result|(bits[i]<<(bits.length-1-i));
        }
        return result;
    }

    public static void main(String[] args){
//        int n = 230;
//        String binaryString = Integer.toBinaryString(n);
//        byte[] data = new byte[8];
//        int index=0;
//        for(int swapNum = 8-binaryString.getBytes().length;index<swapNum;++index){
//            data[index] = 0;
//        }
//
//        for (int i = binaryString.getBytes().length-1; i >=0 ; i--,++index){
//            data[index] = (byte) get(n, i);
//            System.out.print(get(n, i) + "\t");
//        }
//        System.out.println();
//        System.out.println(Arrays.toString(data));
//        System.out.println(combineBit(new byte[]{1,0,1,1,1}));
        new ReceiveSocketService().receiveMessage();
    }



    private static abstract class State{
        public void onExit(){}
        public abstract void onReceiveBit(int bit);
    }


    private class WaitDataState extends State{

        @Override
        public void onReceiveBit(int bit) {
            if(BEGIN_BIT == bit){
                changeState(new DataState());
            }
        }
    }

    private class DataState extends State{
        byte[] data = new byte[8];
        int dataIndex = 0;

        @Override
        public void onReceiveBit(int bit) {
            data[dataIndex] = (byte) bit;
            dataIndex++;
            if(dataIndex == data.length){
                changeState(new EndBitState());
            }
        }

        @Override
        public void onExit() {
            int intData = combineBit(data);
            intData = ~intData;
            String hexData = Integer.toHexString(intData);//16进制
            System.out.println("hextData:"+hexData);
            System.out.println("asic:"+(char)intData);
            EventBus.getDefault().post(new BlueMessageBean(RECEIVER_MESSAGE, hexData));
        }
    }

    private class EndBitState extends State{

        @Override
        public void onReceiveBit(int bit) {
            assert bit == END_BIT;
            changeState(new WaitDataState());
        }
    }

    private void changeState(State newState){
        state.onExit();
        state = newState;
    }

    public void stop(){
        running = false;
    }

    /**
     * 会阻塞，放到其他线程
     */
    public void receiveMessage(){
          if (APP.bluetoothSocket == null){
              return;
          }
        try {
            InputStream inputStream = APP.bluetoothSocket.getInputStream();
            //串口消息 起始位（1）+数据位（8）+停止位（1）
//            {
//                byte[] raw8BitData = new byte[8];
//                int n = 0;//8位数据
//
//                while (running){
//                    while (-1!=(n=inputStream.read())){
////                        String binaryString = Integer.toBinaryString(n);
////                        int index=0;
////                        for(int swapNum = 8-binaryString.getBytes().length;index<swapNum;++index){
////                            raw8BitData[index] = 0;
////                        }
////
////                        for (int i = binaryString.getBytes().length-1; i >=0 ; i--,++index){
////                            raw8BitData[index] = (byte) get(n, i);
////                        }
////
////                        //填充完接收的8位数据信息
////
////                        for(int i=0;i<raw8BitData.length;++i){
////                            state.onReceiveBit(raw8BitData[i]);
////                        }
//
//
//                        state.onReceiveBit(n);
//                    }
//
//                }
//            }
            // 从客户端获取信息
            BufferedReader bff = new BufferedReader(new InputStreamReader(inputStream));
            String json;
            while (true) {
                while ((json = bff.readLine()) != null) {
                    EventBus.getDefault().post(new BlueMessageBean(RECEIVER_MESSAGE,json));
                }
                if ("file".equals(json)) {
                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory() + "/test.gif");
                    int length;
                    int fileSzie = 0;
                    byte[] b = new byte[1024];
                    // 2、把socket输入流写到文件输出流中去
                    while ((length = inputStream.read(b)) != -1) {
                        fos.write(b, 0, length);
                        fileSzie += length;
                        System.out.println("当前大小：" + fileSzie);
                        //这里通过先前传递过来的文件大小作为参照，因为该文件流不能自主停止，所以通过判断文件大小来跳出循环
                    }
                    fos.close();
                    EventBus.getDefault().post(new BlueMessageBean(RECEIVER_FILE,"文件保存成功"));
                }
            }
        } catch (IOException e) {
            MyLog.e(TAG, e.getMessage(), e);
        }

    }
}
