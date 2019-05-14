package ljy.net;

import android.support.annotation.NonNull;

import io.netty.buffer.ByteBuf;
import ljy.msg.RequestMsg;
import ljy.msg.ServerResponse;
import ljy.utils.MyLog;

public abstract class AbsConnectServer {
    protected final String TAG =  this.getClass().getSimpleName();
    protected String host;
    protected int port;
    protected volatile State state = State.INIT;
    protected Callback callback;

    public interface Callback{
        void onConnected();
        void onDisconnected();
        void onReconnected();
        void onSend();
        void onReceived(RequestMsg msg);
        void onError(String msg);
    }
    public enum State{
        INIT,
        CONNECTING,
        CONNECTED,
    }

    public final boolean isNeedReconnect(@NonNull String host, @NonNull int port){
        if(this.host==null){
            return true;
        }
        if(!host.equals(this.host)){
            return true;
        }
        if(port!=this.port){
            return true;
        }
        return false;
    }
    public final void connect(@NonNull String host, @NonNull int port, @NonNull Callback callback){
        assert isNeedReconnect(host, port);
        this.host = host;
        this.port = port;
        this.callback = callback;
        disconnect();
        setState(State.CONNECTING);
        connectImpl();
    }
    protected abstract void connectImpl();

    public abstract void disconnect();

    public abstract void sendMsg(ServerResponse serverResponse);
    public abstract void sendMsg(ByteBuf serverResponse);

    public abstract boolean isActive();
    public void setState(State state){
        MyLog.i(TAG, "oldstate:"+this.state+",newstate:"+state);
        this.state = state;
    }
}
