package com.ljy.misc.net.http;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

public class FullDecodedRequest {
    private final Values values;
    private final String path;

    public FullDecodedRequest(HttpRequest request, Values values) {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        this.path = queryStringDecoder.path();
        this.values = values;
    }

    public Values getValues() {
        return this.values;
    }

    public String getPath() {
        return this.path;
    }
}