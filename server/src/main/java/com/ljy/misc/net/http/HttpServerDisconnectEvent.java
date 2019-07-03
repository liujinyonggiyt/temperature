package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public class HttpServerDisconnectEvent extends HttpServerEvent {
    public HttpServerDisconnectEvent(Channel channel) {
        super(HttpServerEventType.HTTP_DISCONNECT, channel);
    }
}