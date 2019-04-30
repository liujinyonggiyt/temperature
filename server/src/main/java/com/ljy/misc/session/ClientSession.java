package com.ljy.misc.session;

import com.ljy.misc.utils.AnyUtils;
import io.netty.channel.Channel;

/**
 * @author liujinyong
 */
public class ClientSession {
    /**
     * 客户端的链接
     */
    private final Channel channel;

    private final String host;
    /**
     * 独一无二的sessionId
     */
    private long sessionId = -1L;
    /**
     * 客户端上一次ping的时间戳
     */
    private int lastPingSec;
    /**
     * 是否有数据包缓存
     */
    private boolean isHaveCache = false;


    public ClientSession(Channel channel, int lastPingSec) {
        this.channel = channel;
        this.lastPingSec = lastPingSec;
        host= AnyUtils.calcuChannelHost(channel);
    }

    public Channel getChannel() {
        return channel;
    }

    public String getHost() {
        return host;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public int getLastPingSec() {
        return lastPingSec;
    }

    public void setLastPingSec(int lastPingSec) {
        this.lastPingSec = lastPingSec;
    }

    public boolean isHaveCache() {
        return isHaveCache;
    }

    public void setHaveCache(boolean haveCache) {
        isHaveCache = haveCache;
    }
}
