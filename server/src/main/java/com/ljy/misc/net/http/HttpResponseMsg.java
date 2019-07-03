package com.ljy.misc.net.http;

import java.util.LinkedList;
import java.util.List;

public class HttpResponseMsg {
    private final String contentType;
    private volatile LinkedList<HeaderBean> headerList;

    public HttpResponseMsg(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public synchronized void addHeader(String headerType, String headerValue) {
        if (this.headerList == null) {
            this.headerList = new LinkedList();
        }

        this.headerList.add(new HttpResponseMsg.HeaderBean(headerType, headerValue));
    }

    public synchronized List<HeaderBean> getHeaderList() {
        return this.headerList;
    }

    public static final class HeaderBean {
        private final String headerType;
        private final String headerValue;

        public HeaderBean(String headerType, String headerValue) {
            this.headerType = headerType;
            this.headerValue = headerValue;
        }

        public String getHeaderType() {
            return this.headerType;
        }

        public String getHeaderValue() {
            return this.headerValue;
        }
    }
}