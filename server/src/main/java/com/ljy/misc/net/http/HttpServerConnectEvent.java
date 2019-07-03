package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public class HttpServerConnectEvent extends HttpServerEvent {
    public HttpServerConnectEvent(Channel channel) {
        super(HttpServerEventType.HTTP_CONNECT, channel);
    }
}