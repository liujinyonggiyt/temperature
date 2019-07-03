package com.ljy.mrg;

import com.google.inject.Inject;
import com.ljy.misc.AppUpdateInfo;
import com.ljy.misc.msg.SystemTimeMrg;
import com.ljy.misc.net.http.HttpResponseJsonObjMsg;
import com.ljy.misc.net.http.Values;
import com.ljy.misc.trigger.Timer;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Properties;

/**
 * Author:liujinyong
 * Date:2019/7/2
 * Time:17:00
 */
public class AppUpdateMrg {
    private static final Logger logger = LoggerFactory.getLogger(AppUpdateMrg.class);
    private final SystemTimeMrg systemTimeMrg;
    private final JsonMrg jsonMrg;

    private long lastModifiedMillTime;
    private Properties properties = new Properties();
    private String lastVersion = "";
    private long appSize = 0L;
    private String md5 = "";
    @Inject
    public AppUpdateMrg(TimerMrg timerMrg, SystemTimeMrg systemTimeMrg, JsonMrg jsonMrg) {
        this.systemTimeMrg = systemTimeMrg;
        this.jsonMrg = jsonMrg;
        timerMrg.addTimer(new Timer(10000, Integer.MAX_VALUE, this::callBack) {

        });
    }

    void callBack(Timer timer) throws Exception{
        File file = new File("app/appUpdate.properties");
        if(!file.exists()){
            return;
        }
        if(file.lastModified() == lastModifiedMillTime){
            return;
        }

        lastModifiedMillTime = file.lastModified();
        properties.clear();
        properties.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));

        File appFile = new File("app/app-release.apk");
        lastVersion = properties.getProperty("versionCode");
        if(!appFile.exists()){
           return;
        }
        appSize = appFile.length();
        md5 = md5(appFile);
        logger.info("check update success!newVersion:"+lastVersion);

    }

    public static String md5(File file) {
        MessageDigest digest = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];

        try {
            if (!file.isFile()) {
                return "";
            }

            digest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);

            while (true) {
                int len;
                if ((len = fis.read(buffer, 0, 1024)) == -1) {
                    fis.close();
                    break;
                }

                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        BigInteger var5 = new BigInteger(1, digest.digest());
        return String.format("%1$032x", new Object[]{var5});
    }

    public void checkUpdate(Channel channel, String path, Values values) throws Exception{
        String oldVersion = values.getAsString("version");
        AppUpdateInfo updateInfo = new AppUpdateInfo();
        updateInfo.setVersionCode(lastVersion);
        if(oldVersion.equals(lastVersion)){
            updateInfo.setHasUpdate(false);
        }else {
            updateInfo.setHasUpdate(true);
            updateInfo.setUpdateContent(properties.getProperty("updateContent", ""));
            updateInfo.setVersionName(properties.getProperty("versionName", ""));
            updateInfo.setUrl(properties.getProperty("url", ""));
            updateInfo.setMd5(md5);
            updateInfo.setSize(appSize);
            updateInfo.setIsForce(properties.getProperty("isForce", "false"));
            updateInfo.setIsIgnorable(properties.getProperty("isIgnorable", "false"));
            updateInfo.setIsSilen(properties.getProperty("isSilent", "false"));
        }

        channel.writeAndFlush(new HttpResponseJsonObjMsg(updateInfo));

    }
}
