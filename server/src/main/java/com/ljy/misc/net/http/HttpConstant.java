package com.ljy.misc.net.http;

import io.netty.util.AttributeKey;

public class HttpConstant {
    public static AttributeKey<ChannelAttachmentData> HTTP_CHANNEL_ATTACHMENT = AttributeKey.valueOf("HTTP_CHANNEL_ATTACHMENT");

    public HttpConstant() {
    }
}