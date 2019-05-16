package com.ljy.misc.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by lishile on 2016/3/7.
 */
public class BroadcastChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeLine = ch.pipeline();

        pipeLine.addLast(new BroadcastChannelDecoder());
    }
}
