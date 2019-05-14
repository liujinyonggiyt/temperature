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
	public int getTotalBytes() {
		return buffer.length+4;
	}
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
//		try {
//			msgcode = util.readInt();
//		} catch (IOException e) {
//		}
	}
    
	public DataIOUtil getUtil() {
		return util;
	}

	public int getMsgCode() {
		return msgcode;
	}

	public byte getByte() throws IOException {
		return util.readByte();
	}

	public short getShort() throws IOException {
		return util.readShort();
	}

	public int getInt() throws IOException {
		return util.readInt();
	}

	public long getLong() throws IOException {
		return util.readLong();
	}

	public float getFloat() throws IOException {
		return util.readFloat();
	}

	public double getDouble() throws IOException {
		return util.readDouble();
	}

	public String getString() throws IOException {
		return util.readUTF();
	}
	public Object getObj() throws IOException, ClassNotFoundException {
		return util.readObj();
	}
	

}
