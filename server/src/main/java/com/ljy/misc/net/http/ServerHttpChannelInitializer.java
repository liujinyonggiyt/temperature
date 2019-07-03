package com.ljy.misc.net.http;

import com.ljy.misc.net.DefaultExceptionHandler;
import com.ljy.mrg.HttpEventQueueMrg;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;


public class ServerHttpChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final HttpEventQueueMrg httpEventQueueMrg;

    public ServerHttpChannelInitializer(HttpEventQueueMrg httpEventQueueMrg) {
        this.httpEventQueueMrg = httpEventQueueMrg;
    }

    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ChannelHandler[]{new HttpRequestDecoder()});
        p.addLast(new ChannelHandler[]{new HttpObjectAggregator(1048576)});
        p.addLast(new ChannelHandler[]{new HttpVerifyDecoder()});
        p.addLast(new ChannelHandler[]{new FormPayloadDecoder()});
        p.addLast(new ChannelHandler[]{new WebLogicThreadDecoder(this.httpEventQueueMrg)});
        p.addLast(new ChannelHandler[]{new HttpResponseEncoder()});
        p.addLast(new ChannelHandler[]{new FirstHttpEncoder()});
        p.addLast(new ChannelHandler[]{new DefaultExceptionHandler()});
    }
}