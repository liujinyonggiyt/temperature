package ljy.msg;

import java.io.IOException;

/**
 * 此接口声明了对客户端消息的读取函数，由消息的消费者{@link MsgProcessor}使用。
 *
 * 游戏协议是变长的，基于byte stream的协议。使用mina的IoBuffer包装，进行读取最为理想。 <b
 * 但mina规定,解码后变成自定义的消息类型，IoHandler.messageReceived()才会被调用。
 * 
 * 
 * @author wutao
 */
public interface RequestMsg{
}