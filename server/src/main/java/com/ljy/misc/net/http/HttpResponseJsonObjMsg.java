package com.ljy.misc.net.http;

public class HttpResponseJsonObjMsg extends HttpResponseMsg {
    private final Object msg;

    public HttpResponseJsonObjMsg(Object msg) {
        super("application/json");
        this.msg = msg;
    }

    public HttpResponseJsonObjMsg(String contentType, Object msg) {
        super(contentType);
        this.msg = msg;
    }

    public Object getMsg() {
        return this.msg;
    }
}