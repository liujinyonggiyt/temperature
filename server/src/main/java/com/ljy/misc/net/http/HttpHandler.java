package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public interface HttpHandler {
    void handler(Channel var1, String var2, Values var3) throws Exception;
}