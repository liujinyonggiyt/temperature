package com.ljy.misc.net.http;

import io.netty.channel.Channel;

public class HttpServerOnEventEvent extends HttpServerEvent {
    private final String path;
    private final Values values;

    public HttpServerOnEventEvent(Channel channel, String path, Values values) {
        super(HttpServerEventType.HTTP_EVENT, channel);
        this.path = path;
        this.values = values;
    }

    public String getPath() {
        return this.path;
    }

    public Values getValues() {
        return this.values;
    }
}