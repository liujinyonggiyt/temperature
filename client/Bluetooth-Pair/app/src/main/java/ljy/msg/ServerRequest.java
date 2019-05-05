package ljy.msg;

import java.io.IOException;

/**
 * {@link RequestMsg}的一个实现。<br/>
 * 使用byte数组作为缓冲区，使用{@link DataIOUtil}将byte数组转成合适的数据。
 * 
 * @author wutao
 * 
 */
public class ServerRequest implements RequestMsg {
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
	
	public ServerRequest(byte[] array) throws IllegalArgumentException {
		if (array == null) {
			throw new IllegalArgumentException("��Ϣ����������Ϊnull");
		}
		if (array.length == 0) {
			throw new IllegalArgumentException("��Ϣ�����������СΪ0");
		}
		buffer = array;

		util = DataIOUtil.newInstance4In(array);
		try {
			msgcode = util.readInt();
		} catch (IOException e) {
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
