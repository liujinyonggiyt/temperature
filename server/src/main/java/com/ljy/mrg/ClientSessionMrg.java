package com.ljy.mrg;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ljy.misc.session.ClientSession;
import com.ljy.misc.timer.TimerCloseNettyChannel;
import io.netty.channel.Channel;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liujinyong 客户端会话管理器
 */
public class ClientSessionMrg {
    /**
     * 定时任务管理器
     */
    private final TimerMrg timerMrg;
    
    /**
     * 当前进程在场景中所有玩家的链接对象和会话之间的映射
     */
    private final Map<Channel, ClientSession> channelAndClientSession = new HashMap<>();
    /**
     * 当前进程在场景中所有玩家的链接的sessionId(也同时是角色id)和会话之间的映射
     */
    private final Long2ObjectMap<ClientSession> sessionIdAndSession = new Long2ObjectOpenHashMap<>();

    @Inject
    public ClientSessionMrg(TimerMrg timerMrg) {
        this.timerMrg = timerMrg;
    }

    /**
     * 增加一个客户端会话,该参数传入的会话对象一定处于ClientSessionRegistState状态
     *
     * @param clientSession
     */
    public void addSession(ClientSession clientSession) {
        Preconditions.checkArgument(!channelAndClientSession.containsKey(clientSession.getChannel()),
                "client session duplicate");
        Preconditions.checkArgument(!sessionIdAndSession.containsKey(clientSession.getSessionId()),
                "client session duplicate");
        channelAndClientSession.put(clientSession.getChannel(), clientSession);
        sessionIdAndSession.put(clientSession.getSessionId(), clientSession);
    }

    /**
     * 根据会话id找其对应的会话
     *
     * @param sessionId
     * @return
     */
    public ClientSession getSessionBySessionId(long sessionId) {
        return sessionIdAndSession.get(sessionId);
    }

    /**
     * 根据客户端的连接寻找到客户端对应的session
     *
     * @param channel
     * @return
     */
    public ClientSession getSessionByChannel(Channel channel) {
        return channelAndClientSession.get(channel);
    }

    /**
     * 移除一个客户端会话,并关闭socket连接
     *
     * @param sessionId
     * @return
     */
    public ClientSession removeSession(long sessionId) {
        ClientSession clientSession = sessionIdAndSession.remove(sessionId);
        if (null == clientSession) {
            throw new IllegalArgumentException(
                    "error: remove a not exist player");
        }
        Channel clientChannel = clientSession.getChannel();
        channelAndClientSession.remove(clientChannel);
        return clientSession;
    }

    public Map<Channel, ClientSession> getChannelAndClientSession() {
        return channelAndClientSession;
    }

    /**
     * 延迟关闭客户端session中的连接
     *
     * @param channel
     */
    public void delayDisconnect(Channel channel) {
        timerMrg.addTimer(new TimerCloseNettyChannel(20 * 1000L, channel));
    }

    public void flush(ClientSession clientSession) {
        Channel clientChannel = clientSession.getChannel();
        if (null == clientChannel) {
            return;
        }
        clientChannel.flush();
        clientSession.setHaveCache(false);
    }

    public Iterator<ClientSession> sceneSessionIter() {
        return sessionIdAndSession.values().iterator();
    }



}
