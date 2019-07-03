package com.ljy.misc.net.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author:liujinyong
 * Date:2019/5/16
 * Time:17:04
 */
public class GroupBroadcastChannelDecoder extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(GroupBroadcastChannelDecoder.class);
    private static final ChannelGroup BROAD_CHANNELS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * key:ip
     */
    private static final ConcurrentHashMap<String, Set<Channel>> listenIpAndGroup = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Channel, String> channelAndListenIp = new ConcurrentHashMap<>();
    private static Object groupLock = new Object();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        BROAD_CHANNELS.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel channel = ctx.channel();
        if(BROAD_CHANNELS.contains(channel)){
            BROAD_CHANNELS.remove(channel);
        }else{
            unRegistGroup(channel);
        }

        //wifi模块
        String ip = getIp(channel);
        Set<Channel> set = listenIpAndGroup.get(ip);
        if(null!=set){
            for(Channel appChannel:set){
                appChannel.writeAndFlush("unbind:"+"\r\n");
            }
            listenIpAndGroup.remove(ip);
        }
    }

    private void unRegistGroup(Channel channel){
        synchronized (groupLock){
            String ip = channelAndListenIp.get(channel);
            if(null!=ip){//app
                Set<Channel> set = listenIpAndGroup.get(ip);
                if(null!=set){
                    set.remove(channel);
                }
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(msg.contains("bind:")){//app消息
            Channel channel = ctx.channel();
            String ip = msg.replace("bind:", "");

            unRegistGroup(channel);

            synchronized (groupLock){
                channelAndListenIp.put(channel, ip);
                Set<Channel> set = listenIpAndGroup.get(ip);
                if(null!=set){
                    set.add(channel);
                }else{
                    set = new HashSet<>();
                    set.add(channel);
                    listenIpAndGroup.put(ip, set);
                }
            }

            BROAD_CHANNELS.remove(channel);

            channel.writeAndFlush("bindSuccess:"+ip+"\r\n");
        }else{//wifi模块消息
            String ip = getIp(ctx.channel());
            Set<Channel> groupChannels = listenIpAndGroup.get(ip);
            if(null!=groupChannels){//组广播
                for(Channel channel:groupChannels){
                    channel.writeAndFlush(msg+"\r\n");
                }
            }else{//广播
                BROAD_CHANNELS.writeAndFlush(msg+"\r\n");
            }
        }
    }

    private String getIp(Channel channel){
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        return address.getAddress().getHostAddress();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.error("", cause);
    }
}
