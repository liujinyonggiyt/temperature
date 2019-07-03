package com.ljy.misc.net.http;

public enum HttpServerEventType {
    HTTP_CONNECT,
    HTTP_EVENT,
    HTTP_DISCONNECT;

    private HttpServerEventType() {
    }
}