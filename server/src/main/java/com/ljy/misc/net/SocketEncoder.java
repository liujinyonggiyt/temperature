package com.ljy.misc.net;

import com.ljy.misc.msg.ServerResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketEncoder extends ChannelOutboundHandlerAdapter
{
	private static final Logger logger = LoggerFactory
			.getLogger(SocketEncoder.class);

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
                      ChannelPromise promise) throws Exception
	{

		try
		{
			if(msg instanceof ServerResponse)
			{
				ServerResponse serverResponse = (ServerResponse) msg;
				writeImpl(ctx,promise, serverResponse);
			}
			else
			{
				logger.error("error: send proto class type error :"
						+ msg.getClass().getSimpleName());
			}
		}
		catch(Exception e)
		{
			logger.error("",e);
		}

	}

	protected void writeImpl(ChannelHandlerContext ctx, ChannelPromise promise,
							 ServerResponse serverResponse) throws Exception
	{
		byte[] body = serverResponse.getBodyBytes();
		// proto对象序列化之后的协议长度
		// 最后经过序列化的协议中包头前四个字节所标识的协议长度,其中1表示的是协议枚举对应字符串的长度,用一个字节来表示
		final int finalProtoLen = body.length + 4+4;
		ByteBuf byteBuf = null;
		byteBuf = ctx.alloc().buffer(finalProtoLen,finalProtoLen);

		// 将协议的长度写入进来
		byteBuf.writeInt(body.length + 4);
		byteBuf.writeInt(serverResponse.getMsgCode());
		byteBuf.writeBytes(body);
		try(ByteBufOutputStream out = new ByteBufOutputStream(byteBuf))
		{
			writeBuffer(ctx,out.buffer(),promise);
		}

	}

	protected void writeBuffer(ChannelHandlerContext ctx,
                               ByteBuf finalBuf, ChannelPromise promise) throws Exception
	{
		super.write(ctx,finalBuf,promise);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		// 走到这里的原因有多种
		ctx.channel().close();
		logger.debug("my be remove peer close process manully");
	}
}
