package com.ljy.mrg;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ljy.misc.net.http.*;

import java.util.HashMap;
import java.util.Map;

public class HttpEventHandlerMrg {
    private final HttpEventQueueMrg httpEventQueueMrg;
    private final Map<String, HttpHandler> pathAndHandler = new HashMap();
    private volatile HttpConnectHandler connectHandler;
    private volatile HttpDisconnectHandler disconnectHandler;

    @Inject
    public HttpEventHandlerMrg(HttpEventQueueMrg httpEventQueueMrg) {
        this.httpEventQueueMrg = httpEventQueueMrg;
    }

    public void registerConnectHandler(HttpConnectHandler httpConnectHandler) {
        Preconditions.checkArgument(this.connectHandler == null);
        this.connectHandler = httpConnectHandler;
    }

    public void registerDisconnectHandler(HttpDisconnectHandler httpDisconnectHandler) {
        Preconditions.checkArgument(this.disconnectHandler == null);
        this.disconnectHandler = httpDisconnectHandler;
    }

    public void registerHandler(String path, HttpHandler httpHandler) {
        this.pathAndHandler.put(path, httpHandler);
    }

    public void tick() throws Exception {
        while(!this.httpEventQueueMrg.getQueue().isEmpty()) {
            HttpServerEvent evt = (HttpServerEvent)this.httpEventQueueMrg.getQueue().poll();
            switch(evt.getEvtType()) {
            case HTTP_CONNECT:
                if (this.connectHandler != null) {
                    this.connectHandler.onConnect(evt.getChannel());
                }
                break;
            case HTTP_DISCONNECT:
                if (this.disconnectHandler != null) {
                    this.disconnectHandler.onDisconnect(evt.getChannel());
                }

                evt.getChannel().close();
                break;
            case HTTP_EVENT:
                HttpServerOnEventEvent event = (HttpServerOnEventEvent)evt;
                HttpHandler handler = (HttpHandler)this.pathAndHandler.get(event.getPath());
                if (handler != null) {
                    System.out.println(String.format("http: %s", event.getPath()));
                    handler.handler(evt.getChannel(), event.getPath(), event.getValues());
                }
                break;
            default:
                throw new RuntimeException();
            }
        }

    }
}