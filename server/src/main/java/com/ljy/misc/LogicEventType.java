package com.ljy.misc;

/**
 * Author:liujinyong
 * Date:2019/2/21
 * Time:11:31
 */
public enum LogicEventType {
    /**
     * 收到客户端发过来协议包
     */
    LOGIC_CLIENT_PROTO_COMMING_EVENT,
    /**
     * 客户端刚刚建立连接还没有注册
     */
    LOGIC_CLIENT_REGISTING,
    /**
     * 客户端断开连接
     */
    LOGIC_CLIENT_DISCONNECT,
    /**
     * 服务器断开连接
     */
    LOGIC_SERVER_DISCONNECT,
}
