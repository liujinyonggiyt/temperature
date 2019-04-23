package com.ljy.misc.msg;

import java.io.*;

/**
 * ��һ��byteArray�����д�Ĺ����࣬ʹ�ú�ǵ�close��
 * 
 * @author wutao
 * 
 */
public class DataIOUtil {
//	private boolean read = true;
	private ByteArrayInputStream in;
	private ByteArrayOutputStream out;

	private DataInputStream dataIn;
	private DataOutputStream dataOut;

	public static DataIOUtil newInstance4In(byte[] buffer) {
		return new DataIOUtil(buffer);

	}

	public static DataIOUtil newInstance4Out() {
		return new DataIOUtil();
	}

	private DataIOUtil(byte[] buffer) {
//		read = true;
		in = new ByteArrayInputStream(buffer);
		dataIn = new DataInputStream(in);
	}

	private DataIOUtil() {
//		read = false;
		out = new ByteArrayOutputStream();
		dataOut = new DataOutputStream(out);
	}

	public byte readByte() throws IOException {
		return dataIn.readByte();
	}

	public int read() throws IOException {
		return dataIn.read();
	}

	public short readShort() throws IOException {
		return dataIn.readShort();
	}

	public int readInt() throws IOException {
		return dataIn.readInt();
	}

	public long readLong() throws IOException {
		return dataIn.readLong();
	}

	public float readFloat() throws IOException {
		return dataIn.readFloat();
	}

	public double readDouble() throws IOException {
		return dataIn.readDouble();
	}

	public String readUTF() throws IOException {
		return dataIn.readUTF();
	}

	public void writeByte(int value) throws IOException {
		dataOut.writeByte(value);
	}

	public void writeBoolean(boolean value) throws IOException {
		dataOut.writeBoolean(value);
	}

	public void writeBytes(byte[] value) throws IOException {
		dataOut.write(value);
	}

	public void writeShort(int value) throws IOException {
		dataOut.writeShort(value);
	}

	public void writeInt(int value) throws IOException {
		dataOut.writeInt(value);
	}

	public void writeLong(long value) throws IOException {
		dataOut.writeLong(value);
	}

	public void writeFloat(float value) throws IOException {
		dataOut.writeFloat(value);
	}

	public void writeDouble(double value) throws IOException {
		dataOut.writeDouble(value);
	}

	public void writeUTF(String value) throws IOException {
		dataOut.writeUTF(value);
	}
	public void writeObj(Object obj) throws IOException{
		ByteArrayOutputStream bao=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(bao);
		oos.writeObject(obj);
		oos.flush();
		byte[] t=bao.toByteArray();
		dataOut.writeInt(t.length);
		dataOut.write(t);
	}
	public Object readObj() throws IOException, ClassNotFoundException{
		int t=dataIn.readInt();
		byte[] a=new byte[t];
		dataIn.read(a);
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(a));
		Object obj=ois.readObject();
		return obj;
	}
	

	/**
	 * ���ʻ� ��Ϣ����
	 * 
	 * @param value
	 * @throws IOException
	 */
	public void writeInterUTF(int msgkey) throws IOException {
	 //dataOut.writeUTF(5+"");//Ĭ��λ��
     dataOut.writeInt(msgkey);
     dataOut.writeByte(0);
	}
	public void writeInterUTF(int msgkey,String... vars) throws IOException { 
		//dataOut.writeUTF(position+"");	
		if(vars==null)
		{
			writeInterUTF(msgkey);
		}else
		{   
			dataOut.writeInt(msgkey);
			dataOut.writeByte(vars.length);
			for (String var : vars) {
				dataOut.writeUTF(var);
			}
		}
		
	}
	public byte[] toByteArray() {
		return this.out.toByteArray();
	}

}
