package ljy.msg;

import java.io.IOException;

import io.netty.buffer.ByteBuf;
public class ServerResponse implements ResponseMsg {
	private DataIOUtil output = DataIOUtil.newInstance4Out();
	private int msgCode;

	public ServerResponse(com.ljy.ProtoEnum protoEnum) {
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
    public void setMsgCode(int code) {
        this.msgCode = code;
    }
}
