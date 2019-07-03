package com.ljy.misc.net.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public class HttpVerifyDecoder extends SimpleChannelInboundHandler<HttpObject> {
    public HttpVerifyDecoder() {
    }

    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        DecoderResult result = httpObject.decoderResult();
        if (!result.isSuccess()) {
            try {
                ctx.channel().close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        } else {
            if (httpObject instanceof HttpRequest) {
                HttpRequest httpRequest = (HttpRequest)httpObject;
                ctx.fireChannelRead(httpRequest);
            }

        }
    }
}