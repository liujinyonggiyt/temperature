package com.ljy.misc.msg;

import java.io.IOException;

/**
 * �˽ӿ������˶Կͻ�����Ϣ�Ķ�ȡ����������Ϣ��������{@link MsgProcessor}ʹ�á�
 * 
 * ��ϷЭ���Ǳ䳤�ģ�����byte stream��Э�顣ʹ��mina��IoBuffer��װ�����ж�ȡ��Ϊ���롣 <br/>
 * ��mina�涨,��������Զ������Ϣ���ͣ�IoHandler.messageReceived()�Żᱻ���á�
 * 
 * 
 * @author wutao
 */
public interface RequestMsg {
	//�����Ϣ��
	public int getMsgCode();
	public int getTotalBytes();
	public void BitReversion();
	//��õõ���Ϣʱ��ʱ��
	public long getReceiveTime();
	public DataIOUtil getUtil();
	public byte getByte() throws IOException;

	public short getShort() throws IOException;

	public int getInt() throws IOException;

	public long getLong() throws IOException;

	public float getFloat() throws IOException;

	public double getDouble() throws IOException;

	public String getString() throws IOException;
	public Object getObj() throws IOException,ClassNotFoundException;
}