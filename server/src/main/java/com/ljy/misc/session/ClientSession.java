package com.ljy.misc.session;

import com.ljy.misc.utils.AnyUtils;
import io.netty.channel.Channel;

/**
 * @author lishile
 */
public class ClientSession {
    /**
     * 独一无二的sessionId
     */
    private long sessionId;
    /**
     * 客户端的链接
     */
    private Channel channel;
    /**
     * 客户端上一次ping的时间戳
     */
    private int lastPingSec;
    /**
     * 是否有数据包缓存
     */
    private boolean isHaveCache = false;
    /**
     * 玩家所在场景
     */
    private long sceneGuid;

    /**
     * 玩家当前验证令牌
     */
    private String token;

    private String host;
    
    private ClientProtoStaistic protoStaisticInfo;

    public ClientSession(long sessionId, Channel channel, int lastPingSec, long sceneGuid, String token) {
        this.sessionId = sessionId;
        this.channel = channel;
        this.lastPingSec = lastPingSec;
        this.sceneGuid=sceneGuid;
        this.token = token;
        host= AnyUtils.calcuChannelHost(channel);
        setProtoStaisticInfo(new ClientProtoStaistic(lastPingSec, 0));
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getSceneGuid() {
        return sceneGuid;
    }

    public void setSceneGuid(long sceneGuid) {
        this.sceneGuid = sceneGuid;
    }

    public boolean isHaveCache() {
        return isHaveCache;
    }

    public void setHaveCache(boolean isHaveCache) {
        this.isHaveCache = isHaveCache;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public int getLastPingSec() {
        return lastPingSec;
    }

    public void setLastPingSec(int lastPingSec) {
        this.lastPingSec = lastPingSec;
    }

    public ClientProtoStaistic getProtoStaisticInfo() {
		return protoStaisticInfo;
	}

	public void setProtoStaisticInfo(ClientProtoStaistic protoStaisticInfo) {
		this.protoStaisticInfo = protoStaisticInfo;
	}

	public static final class ClientProtoStaistic{
    	
    	public ClientProtoStaistic(int secondTime, int recvProtoNum) {
			super();
			this.secondTime = secondTime;
			this.recvProtoNum = recvProtoNum;
		}
		private int secondTime;
    	private int recvProtoNum;
		public int getSecondTime() {
			return secondTime;
		}
		public void setSecondTime(int secondTime) {
			this.secondTime = secondTime;
		}
		public int getRecvProtoNum() {
			return recvProtoNum;
		}
		public void setRecvProtoNum(int recvProtoNum) {
			this.recvProtoNum = recvProtoNum;
		}
    	
    }
}
