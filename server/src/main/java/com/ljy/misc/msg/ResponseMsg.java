package com.ljy.misc.msg;


import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 此接口声明了响应消息编码函数，服务端生产的每个响应消息都必须实现此接口。<br>
 * 这里声明的函数在protocolEncoder中会被自动调用，编码成byte stream发送到客户端。
 * 
 * @author liuhengli
 * 
 */
public interface ResponseMsg {
	/**
	 * 获得消息号 
	 * @return
	 */
	public int getMsgCode();
	
	public int getTotalBytes();
	/**
	 * 设置消息号
	 * @param code
	 */
	public void setMsgCode(int code); 

	void writeBoolean(boolean value) throws IOException;
	void writeBytes(byte[] value) throws IOException;
	void writeShort(int value) throws IOException;
	void writeInt(int value) throws IOException;
	void writeLong(long value) throws IOException;
	void writeFloat(float value) throws IOException;
	void writeDouble(double value) throws IOException;
	void writeUTF(String value) throws IOException;

}
