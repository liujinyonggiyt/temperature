package ljy.base.bean;

/**
 * 发送消息 Eventbus对应的实体类
 */
public class SocketMessageBean {
    public static final int SOCKET_CONNECTED = 1;
    public static final int SOCKET_DISCONNECTED = 2;
    public static final int ON_RECEIVE_MSG = 3;
    private int id;
    private String content;

    public SocketMessageBean(int id) {
        this.id = id;
    }

    public SocketMessageBean(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
