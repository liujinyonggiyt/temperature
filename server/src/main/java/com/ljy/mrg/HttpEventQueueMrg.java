package com.ljy.mrg;

import com.google.inject.Inject;
import com.ljy.misc.net.http.HttpServerEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HttpEventQueueMrg {
    private final ConcurrentLinkedQueue<HttpServerEvent> queue = new ConcurrentLinkedQueue();

    @Inject
    public HttpEventQueueMrg() {
    }

    public ConcurrentLinkedQueue<HttpServerEvent> getQueue() {
        return this.queue;
    }
}