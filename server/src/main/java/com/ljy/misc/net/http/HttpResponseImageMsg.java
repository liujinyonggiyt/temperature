package com.ljy.misc.net.http;

public class HttpResponseImageMsg extends HttpResponseMsg {
    private final byte[] imageFiles;

    public HttpResponseImageMsg(byte[] imageFiles) {
        super("image/png");
        this.imageFiles = imageFiles;
    }

    public byte[] getImageFiles() {
        return this.imageFiles;
    }
}