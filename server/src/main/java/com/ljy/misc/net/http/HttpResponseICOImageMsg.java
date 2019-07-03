package com.ljy.misc.net.http;

public class HttpResponseICOImageMsg extends HttpResponseMsg {
    private final byte[] imageFiles;

    public HttpResponseICOImageMsg(byte[] imageFiles) {
        super("image/x-icon");
        this.imageFiles = imageFiles;
    }

    public byte[] getImageFiles() {
        return this.imageFiles;
    }
}
