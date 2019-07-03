package com.ljy.misc.net.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Iterator;
import java.util.List;

public class FirstHttpEncoder extends ChannelOutboundHandlerAdapter {
    private static final ThreadLocal<Gson> gson = new ThreadLocal<Gson>() {
        protected Gson initialValue() {
            Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
            return gson;
        }
    };

    public FirstHttpEncoder() {
    }

    private FirstHttpEncoder.SE createSE(Object msg) {
        if (msg instanceof String) {
            return new FirstHttpEncoder.StringSE();
        } else if (msg instanceof HttpResponseJsonObjMsg) {
            return new FirstHttpEncoder.ResponseJsonObjMsg((HttpResponseMsg)msg);
        }  else if (msg instanceof HttpResponseImageMsg) {
            return new FirstHttpEncoder.ResponseImageMsg((HttpResponseMsg)msg);
        } else {
            return (FirstHttpEncoder.SE)(msg instanceof HttpResponseICOImageMsg ? new FirstHttpEncoder.ResponseICOImageMsg((HttpResponseMsg)msg) : new FirstHttpEncoder.JsonObjSE());
        }
    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        FirstHttpEncoder.SE se = this.createSE(msg);
        ByteBuf buf = se.createBuf(msg);
        HttpResponseStatus status = se.getResponseStates();
        FullHttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf);
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, se.getContentType());
        httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
        httpResponse.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT,DELETE");
        httpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
        httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        synchronized(msg) {
            List<HttpResponseMsg.HeaderBean> headerList = se.getHeaderList();
            if (headerList != null) {
                Iterator var10 = headerList.iterator();

                while(var10.hasNext()) {
                    HttpResponseMsg.HeaderBean headerBean = (HttpResponseMsg.HeaderBean)var10.next();
                    httpResponse.headers().set(headerBean.getHeaderType(), headerBean.getHeaderValue());
                }
            }
        }

        ctx.writeAndFlush(httpResponse, promise);
    }

    private static class ResponseICOImageMsg extends FirstHttpEncoder.ResponseMSGSe {
        ResponseICOImageMsg(HttpResponseMsg msg) {
            super(msg);
        }

        public ByteBuf createBuf(Object obj) throws Exception {
            HttpResponseICOImageMsg abb = (HttpResponseICOImageMsg)obj;
            ByteBuf buf = Unpooled.buffer(abb.getImageFiles().length);
            buf.writeBytes(abb.getImageFiles());
            return buf;
        }
    }

    private static class ResponseImageMsg extends FirstHttpEncoder.ResponseMSGSe {
        ResponseImageMsg(HttpResponseMsg msg) {
            super(msg);
        }

        public ByteBuf createBuf(Object obj) throws Exception {
            HttpResponseImageMsg abb = (HttpResponseImageMsg)obj;
            ByteBuf buf = Unpooled.buffer(abb.getImageFiles().length);
            buf.writeBytes(abb.getImageFiles());
            return buf;
        }
    }

    private static class ResponseJsonObjMsg extends FirstHttpEncoder.ResponseMSGSe {
        ResponseJsonObjMsg(HttpResponseMsg msg) {
            super(msg);
        }

        public ByteBuf createBuf(Object obj) throws Exception {
            String json = ((Gson)FirstHttpEncoder.gson.get()).toJson(((HttpResponseJsonObjMsg)obj).getMsg());
            return Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        }
    }

    private static class ResponseMSGSe extends FirstHttpEncoder.SE {
        protected final HttpResponseMsg msg;

        ResponseMSGSe(HttpResponseMsg msg) {
            this.msg = msg;
        }

        public final String getContentType() {
            return this.msg.getContentType();
        }

        public List<HttpResponseMsg.HeaderBean> getHeaderList() {
            return this.msg.getHeaderList();
        }
    }

    private static class JsonObjSE extends FirstHttpEncoder.SE {
        private JsonObjSE() {

        }

        public ByteBuf createBuf(Object obj) throws Exception {
            String json = ((Gson)FirstHttpEncoder.gson.get()).toJson(obj);
            return Unpooled.copiedBuffer(json, CharsetUtil.UTF_8);
        }
    }

    private static class StringSE extends FirstHttpEncoder.SE {
        private StringSE(){
        }

        public ByteBuf createBuf(Object obj) {
            return Unpooled.copiedBuffer((String)obj, CharsetUtil.UTF_8);
        }
    }

    private static class SE {
        private SE() {
        }

        public HttpResponseStatus getResponseStates() {
            return HttpResponseStatus.OK;
        }

        public List<HttpResponseMsg.HeaderBean> getHeaderList() {
            return null;
        }

        public String getContentType() {
            return "text/plain";
        }

        public ByteBuf createBuf(Object obj) throws Exception {
            return null;
        }
    }
}