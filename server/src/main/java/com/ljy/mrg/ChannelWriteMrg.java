package com.ljy.mrg;

import com.google.inject.Inject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;


/**
 * Created by lishile
 * 写channel的管理器,作为向channel中发送消息的统一接口
 */
public class ChannelWriteMrg {

    private long totalSendNum=0;

    private int onSecondSendNum =0;

    @Inject
    public ChannelWriteMrg() {
    }

    public ChannelFuture write(Channel channel, Object proto) {
        ++onSecondSendNum;
        ++totalSendNum;
        return channel.write(proto);
    }

    public ChannelFuture writeAndFlush(Channel channel, Object proto) {
        ++onSecondSendNum;
        ++totalSendNum;
        return channel.writeAndFlush(proto);
    }

    public long getTotalSendNum() {
        return totalSendNum;
    }

    public void setTotalSendNum(long totalSendNum) {
        this.totalSendNum = totalSendNum;
    }

    public int getOnSecondSendNum() {
        return onSecondSendNum;
    }

    public void setOnSecondSendNum(int onSecondSendNum) {
        this.onSecondSendNum = onSecondSendNum;
    }
}
