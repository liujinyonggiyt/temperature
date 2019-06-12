package ljy.mrg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTimeMrg {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static class Holder{
        private static SystemTimeMrg instance = new SystemTimeMrg();
    }
    public static SystemTimeMrg getInstance(){
        return Holder.instance;
    }
    private SystemTimeMrg(){

    }
    public int getSysSecTime(){
        return (int) (getSysMillTime()/1000);
    }
    public long getSysMillTime(){
        return System.currentTimeMillis();
    }
    public String getCurTime(){
        return simpleDateFormat.format(new Date());
    }
}
