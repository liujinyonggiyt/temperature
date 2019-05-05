package ljy.msg;


import java.io.IOException;

public interface ResponseMsg {
	public int getMsgCode();
	
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
