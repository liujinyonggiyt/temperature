package com.ljy.misc.msg;

import java.io.IOException;

/**
 * {@link RequestMsg}的一个实现。<br/>
 * 使用byte数组作为缓冲区，使用{@link DataIOUtil}将byte数组转成合适的数据。
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
			throw new IllegalArgumentException("消息缓冲区对象为null");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("消息缓冲区对象大小为0");
		}
		buffer = array;
		// 解压缩之前不做无用功

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
			//消息号(int)4个字节 所以从4以后开始位反 10000外的消息为客户端发来的消息
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
