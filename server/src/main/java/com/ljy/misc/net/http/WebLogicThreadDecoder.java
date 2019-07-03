package com.ljy.misc.net.http;

import com.ljy.mrg.HttpEventQueueMrg;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.util.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class WebLogicThreadDecoder extends SimpleChannelInboundHandler<FullDecodedRequest> {
    private static final Logger logger = LoggerFactory.getLogger(WebLogicThreadDecoder.class);
    private final HttpEventQueueMrg httpEventQueueMrg;

    public WebLogicThreadDecoder(HttpEventQueueMrg httpEventQueueMrg) {
        this.httpEventQueueMrg = httpEventQueueMrg;
    }

    public static String getIp(Channel channel) {
        try {
            InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
            String clientIp = ipSocket.getAddress().getHostAddress();
            return clientIp == null ? "" : clientIp;
        } catch (Exception var3) {
            logger.error("", var3);
            return "";
        }
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        Channel channel = ctx.channel();
        Attribute<ChannelAttachmentData> attributeData = channel.attr(HttpConstant.HTTP_CHANNEL_ATTACHMENT);
        if (attributeData.get() == null) {
            attributeData.setIfAbsent(new ChannelAttachmentData(getIp(channel)));
        }

        ChannelConfig config = channel.config();
        DefaultSocketChannelConfig socketConfig = (DefaultSocketChannelConfig)config;
        socketConfig.setPerformancePreferences(0, 1, 2);
        socketConfig.setAllocator(new UnpooledByteBufAllocator(true));
        this.httpEventQueueMrg.getQueue().offer(new HttpServerConnectEvent(channel));
        ctx.fireChannelRegistered();
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.httpEventQueueMrg.getQueue().offer(new HttpServerDisconnectEvent(ctx.channel()));
        super.channelInactive(ctx);
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullDecodedRequest msg) throws Exception {
        this.httpEventQueueMrg.getQueue().offer(new HttpServerOnEventEvent(ctx.channel(), msg.getPath(), msg.getValues()));
    }
}
