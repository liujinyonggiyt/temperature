package com.ljy.misc.net.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FormPayloadDecoder extends SimpleChannelInboundHandler<HttpRequest> {
    public FormPayloadDecoder() {
        super(false);
    }

    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws IOException, InstantiationException, IllegalAccessException {
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri());
        Map<String, List<String>> requestParameters = queryStringDecoder.parameters();
        Values values = new Values();
        Iterator var6 = requestParameters.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry)var6.next();
            String key = (String)entry.getKey();
            List<String> value = (List)entry.getValue();
            if (value.size() == 1) {
                values.put(key, (String)value.get(0));
            } else {
                values.putStringList(key, value);
            }
        }

        if (httpRequest.method() == HttpMethod.POST) {
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), httpRequest);

            try {
                while(decoder.hasNext()) {
                    InterfaceHttpData httpData = decoder.next();
                    if (httpData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                        Attribute attribute = (Attribute)httpData;
                        values.put(attribute.getName(), URLDecoder.decode(attribute.getValue(), "UTF-8"));
                        attribute.release();
                    }
                }
            } catch (HttpPostRequestDecoder.EndOfDataDecoderException var10) {
                ;
            }

            decoder.destroy();
        }

        FullDecodedRequest decodedRequest = new FullDecodedRequest(httpRequest, values);
        ctx.fireChannelRead(decodedRequest);
    }
}