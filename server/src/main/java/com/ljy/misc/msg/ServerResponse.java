package com.ljy.misc.msg;

import com.ljy.ProtoEnum;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * ����˷����ͻ��˵���Ϣ�� ���з��ظ��ͻ��˵���Ϣ����ü̳�����.<br>
 * �����װ�˻���������ֽڲ�����
 * 
 * @author spring wutao liuhengli modify
 * 
 */
public class ServerResponse implements ResponseMsg {
	private DataIOUtil output = DataIOUtil.newInstance4Out();
	private int msgCode;
	private ByteBuf buf = null;
	private int length;

	public ServerResponse(ProtoEnum protoEnum) {
		msgCode = protoEnum.ordinal();
	}

    @Override
    public void writeBoolean(boolean value) throws IOException {
        output.writeBoolean(value);
    }

    @Override
    public void writeBytes(byte[] value) throws IOException {
        output.writeBytes(value);
    }

    @Override
    public void writeShort(int value) throws IOException {
        output.writeShort(value);
    }

    @Override
    public void writeInt(int value) throws IOException {
        output.writeInt(value);
    }

    @Override
    public void writeLong(long value) throws IOException {
        output.writeLong(value);
    }

    @Override
    public void writeFloat(float value) throws IOException {
        output.writeFloat(value);
    }

    @Override
    public void writeDouble(double value) throws IOException {
        output.writeDouble(value);
    }

    @Override
    public void writeUTF(String value) throws IOException {
        output.writeUTF(value);
    }

    public byte[] getBodyBytes(){
	    return output.toByteArray();
    }

    @Override
	public int getMsgCode() {
		return msgCode;
	}

	@Override
	public int getTotalBytes() {
		return length;
	}

    @Override
    public void setMsgCode(int code) {
        this.msgCode = code;
    }
}
