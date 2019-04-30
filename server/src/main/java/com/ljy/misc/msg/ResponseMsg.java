package com.ljy.misc.msg;


import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * �˽ӿ���������Ӧ��Ϣ���뺯���������������ÿ����Ӧ��Ϣ������ʵ�ִ˽ӿڡ�<br>
 * ���������ĺ�����protocolEncoder�лᱻ�Զ����ã������byte stream���͵��ͻ��ˡ�
 * 
 * @author liuhengli
 * 
 */
public interface ResponseMsg {
	/**
	 * �����Ϣ�� 
	 * @return
	 */
	public int getMsgCode();
	
	public int getTotalBytes();
	/**
	 * ������Ϣ��
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
