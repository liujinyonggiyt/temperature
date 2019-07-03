package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public abstract class HttpServerEvent {
    private final HttpServerEventType evtType;
    private final Channel channel;

    public HttpServerEvent(HttpServerEventType evtType, Channel channel) {
        this.evtType = evtType;
        this.channel = channel;
    }

    public HttpServerEventType getEvtType() {
        return this.evtType;
    }

    public Channel getChannel() {
        return this.channel;
    }
}