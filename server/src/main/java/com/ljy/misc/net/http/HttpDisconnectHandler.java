package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public interface HttpDisconnectHandler {
    void onDisconnect(Channel var1);
}