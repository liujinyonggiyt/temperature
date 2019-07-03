package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public interface HttpConnectHandler {
    void onConnect(Channel var1) throws Exception;
}