package ljy.net;

import com.ljy.ProtoEnum;

import java.net.InetSocketAddress;
import java.util.Arrays;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import ljy.msg.ByteStringRequest;
import ljy.msg.RequestMsg;
import ljy.msg.ServerRequest;
import ljy.msg.ServerResponse;
import ljy.utils.MyLog;

public class NettyConnectServer extends AbsConnectServer {
    private SocketChannel socketChannel;
    @Override
    protected void connectImpl() {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        new Bootstrap()
                .channel(NioSocketChannel.class)
                .group(group)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 30, 0));

                        //指定分隔符
                        ByteBuf[] delimiter = new ByteBuf[] {
                                Unpooled.wrappedBuffer(new byte[] { '\r', '\n' }),
                                Unpooled.wrappedBuffer(new byte[] { '\n' }),//10
                                Unpooled.wrappedBuffer(new byte[] { '\r' }),//13
                        };
                        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, delimiter));

                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                RequestMsg serverRequest = new ByteStringRequest(msg.getBytes());
                                callback.onReceived(serverRequest);
                            }
                        });
                        pipeline.addLast(new SocketEncoder());
                    }
                })
                .connect(new InetSocketAddress(host, port))
                .addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()) {
                        socketChannel = (SocketChannel) future.channel();
                        callback.onConnected();
                        setState(State.CONNECTED);
                    } else {
                        disconnect();
                        // 这里一定要关闭，不然一直重试会引发OOM
                        future.channel().close();
                        group.shutdownGracefully();
                        callback.onDisconnected();
                    }
                });
    }


    private class ProtoDecoderHandle extends LengthFieldBasedFrameDecoder {

        public ProtoDecoderHandle(int maxFrameLength) {
            super(maxFrameLength,0,4,0,4);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            setState(State.INIT);
            callback.onDisconnected();
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            super.userEventTriggered(ctx, evt);
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.WRITER_IDLE) {
                    // 空闲了，发个心跳吧
                    ServerResponse msg = new ServerResponse(ProtoEnum.C_PING);
                    ctx.writeAndFlush(msg);
                }
            }
        }

        @Override
        protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length){
            ByteBuf rtBuf = buffer.slice(index,length);
            byte[] bytes = new byte[rtBuf.readableBytes()];
            rtBuf.readBytes(bytes);

            RequestMsg serverRequest = new ByteStringRequest(bytes);
            callback.onReceived(serverRequest);
            return Unpooled.EMPTY_BUFFER;
        }
    }

    @Override
    public void disconnect() {
        if(null!=socketChannel){
            socketChannel.close();
            socketChannel = null;
        }
    }

    @Override
    public void sendMsg(ServerResponse serverResponse) {
        if(null!=socketChannel){
            socketChannel.writeAndFlush(serverResponse);
        }
    }

    @Override
    public void sendMsg(ByteBuf serverResponse) {
        socketChannel.writeAndFlush(serverResponse);
    }

    @Override
    public boolean isActive() {
        return null!=socketChannel && socketChannel.isActive();
    }
}
