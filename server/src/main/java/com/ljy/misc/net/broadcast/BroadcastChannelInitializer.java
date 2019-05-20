package com.ljy.misc.net.broadcast;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class BroadcastChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeLine = ch.pipeline();

        pipeLine.addLast(new BroadcastChannelDecoder());
    }
}
