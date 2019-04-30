package com.ljy.mrg;

import com.google.inject.Inject;
import com.ljy.misc.session.ClientSession;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lishile
 */
public class SendMrg {

    private static final Logger logger = LoggerFactory.getLogger(SendMrg.class);
    private final ChannelWriteMrg channelWriteMrg;

    @Inject
    public SendMrg(ChannelWriteMrg channelWriteMrg) {
        this.channelWriteMrg = channelWriteMrg;
    }

    /**
     * 发送一条协议给某一个客户端会话,立即发送
     *
     * @param clientSession
     * @param proto
     */
    public void sendToClientForAAAAAAAA(ClientSession clientSession, Object proto) {
        Channel clientChannel = clientSession.getChannel();
        if (null != clientChannel) {
            channelWriteMrg.writeAndFlush(clientChannel, proto);
        }
    }

    /**
     * 发送一条协议给某一个客户端的连接,立即发送
     *
     * @param clientChannel
     * @param proto
     */
    public void sendToClientForAAAAAAAA(Channel clientChannel, Object proto) {
        if (null != clientChannel) {
            channelWriteMrg.writeAndFlush(clientChannel, proto);
        }
    }

}
