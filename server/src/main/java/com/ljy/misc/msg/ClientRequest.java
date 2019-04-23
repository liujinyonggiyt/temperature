package com.ljy.misc.msg;

import java.io.IOException;

/**
 * {@link RequestMsg}��һ��ʵ�֡�<br/>
 * ʹ��byte������Ϊ��������ʹ��{@link DataIOUtil}��byte����ת�ɺ��ʵ����ݡ�
 * 
 * @author wutao
 * 
 */
public class ClientRequest implements RequestMsg {
	DataIOUtil util = null;
	byte[] buffer = null;
	int msgcode;
	long receiveTime=System.currentTimeMillis();
	@Override
	public int getTotalBytes() {
		return buffer.length+4;
	}
	@Override
	public long getReceiveTime() {
		return receiveTime;
	}
	
	public ClientRequest(byte[] array) throws IllegalArgumentException {
		if (array == null) {
			throw new IllegalArgumentException("��Ϣ����������Ϊnull");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("��Ϣ�����������СΪ0");
		}
		buffer = array;
		// ��ѹ��֮ǰ�������ù�

		util = DataIOUtil.newInstance4In(array);
		try {
			msgcode = util.readInt();
		} catch (IOException e) {
		}
	}
    
	public void BitReversion()
	{
		if(this.msgcode>10000)
		{
			//��Ϣ��(int)4���ֽ� ���Դ�4�Ժ�ʼλ�� 10000�����ϢΪ�ͻ��˷�������Ϣ
			DataEncryption.BitReversion(buffer, 4);
		}
	}
	public DataIOUtil getUtil() {
		return util;
	}

	public int getMsgCode() {
		return msgcode;
	}

	@Override
	public byte getByte() throws IOException {
		return util.readByte();
	}

	@Override
	public short getShort() throws IOException {
		return util.readShort();
	}

	@Override
	public int getInt() throws IOException {
		return util.readInt();
	}

	@Override
	public long getLong() throws IOException {
		return util.readLong();
	}

	@Override
	public float getFloat() throws IOException {
		return util.readFloat();
	}

	@Override
	public double getDouble() throws IOException {
		return util.readDouble();
	}

	@Override
	public String getString() throws IOException {
		return util.readUTF();
	}
	@Override
	public Object getObj() throws IOException, ClassNotFoundException {
		return util.readObj();
	}
	

}
