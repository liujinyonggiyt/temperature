package com.ljy.misc.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.Closeable;
import java.net.InetSocketAddress;

/**
 * Author:liujinyong
 * Date:2019/4/23
 * Time:20:19
 */
public class AnyUtils {
    /**
     * 安静的调用Closeable实现对象的close方法,确保不向外抛出异常
     *
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Throwable ta) {
            // logger.error("", ta);
        }
    }

    /**
     * 安静的关闭一个nettyChannel
     *
     * @param nettyChannel
     */
    public static ChannelFuture closeQuietly(Channel nettyChannel) {
        if (null == nettyChannel) {
            return null;
        }
        try {
            return nettyChannel.close();
        } catch (Throwable ta) {
            // logger.error("", ta);
        }
        return null;
    }

    public static String calcuChannelHost(Channel channel) {
        if(channel.remoteAddress()==null){
            return "0.0.0.0";
        }
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return address.getAddress().getHostAddress();
    }
}
