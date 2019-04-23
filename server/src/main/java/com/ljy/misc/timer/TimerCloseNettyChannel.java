package com.ljy.misc.timer;

import com.ljy.misc.trigger.Timer;
import com.ljy.misc.utils.AnyUtils;
import io.netty.channel.Channel;

/**
 * Created by lishile 关闭netty连接的定时器timer
 */
public class TimerCloseNettyChannel extends Timer {

    private final Channel channel;

    public TimerCloseNettyChannel(long delayTime, Channel channel) {
        super(delayTime, 1, null);
        setCallBackFunc(this::closeChannel);
        this.channel = channel;
    }

    private void closeChannel(Timer timer) {
    	AnyUtils.closeQuietly(channel);
    }
}
