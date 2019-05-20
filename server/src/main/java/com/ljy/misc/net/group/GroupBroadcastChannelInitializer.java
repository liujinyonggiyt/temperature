package com.ljy.misc.net.group;

import com.ljy.misc.net.broadcast.BroadcastChannelDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupBroadcastChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeLine = ch.pipeline();
        ByteBuf[] delimiters = new ByteBuf[]{
                Unpooled.wrappedBuffer(new byte[] { '\r', '\n' }),
                Unpooled.wrappedBuffer(new byte[] { '\n' }),//10
                Unpooled.wrappedBuffer(new byte[] { '\r' }),//13
        };
        pipeLine.addLast(new DelimiterBasedFrameDecoder(8192, delimiters));
        pipeLine.addLast(new StringDecoder());
        pipeLine.addLast(new GroupBroadcastChannelDecoder());
        pipeLine.addLast(new StringEncoder());
    }
}
