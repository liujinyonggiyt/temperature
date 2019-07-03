package com.ljy.misc;

/**
 * Author:liujinyong
 * Date:2019/7/2
 * Time:17:05
 */
public class AppUpdateInfo {
    // 是否有新版本
    private boolean hasUpdate;
    // 是否静默下载：有新版本时不提示直接下载
    private String isSilen;
    // 是否强制安装：不安装无法使用app
    private String isForce;
    // 是否可忽略该版本
    private String isIgnorable;

    private String versionCode;
    private String versionName;

    private String updateContent;

    private String url;
    private String md5;
    private long size;

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getIsSilen() {
        return isSilen;
    }

    public void setIsSilen(String isSilen) {
        this.isSilen = isSilen;
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

    public String getIsIgnorable() {
        return isIgnorable;
    }

    public void setIsIgnorable(String isIgnorable) {
        this.isIgnorable = isIgnorable;
    }
}
