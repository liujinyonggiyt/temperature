package ljy.base.bean;

import ljy.msg.RequestMsg;

/**
 * 发送消息 Eventbus对应的实体类
 */
public class SocketMessageBean {
    public static final int SOCKET_CONNECTED = 1;
    public static final int SOCKET_DISCONNECTED = 2;
    public static final int ON_RECEIVE_MSG = 3;
    private int id;
    private RequestMsg msg;

    public SocketMessageBean(int id) {
        this.id = id;
    }

    public SocketMessageBean(int id, RequestMsg serverRequest) {
        this.id = id;
        this.msg = serverRequest;
    }

    public int getId() {
        return id;
    }

    public RequestMsg getMsg() {
        return msg;
    }
}
