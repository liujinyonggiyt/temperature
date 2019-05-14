package ljy.msg;

public class ByteStringRequest implements RequestMsg{
    private byte[] bytes;

    public ByteStringRequest(byte[] buffer) {
        this.bytes = buffer;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
