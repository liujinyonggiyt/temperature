package com.ljy.misc.net;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessage.Builder;
import com.google.protobuf.Parser;
import com.gyyx.dragon.utils.AnyUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SocketEncoder extends ChannelOutboundHandlerAdapter
{
	private static final Logger logger = LoggerFactory
			.getLogger(SocketEncoder.class);

	private int sequence=0;
	protected static final ThreadLocal<EncoderMrg> localEncoderMrg = new ThreadLocal<EncoderMrg>()
	{
		protected EncoderMrg initialValue()
		{
			return new EncoderMrg();
		}
	};

	protected static class EncoderMrg
	{

		private final Map<Parser<?>,ProtoEnum> maps = new HashMap<>(
				Short.MAX_VALUE,0.5f);

		public ProtoEnum get(GeneratedMessage protoObj)
		{

			Parser<?> parser = protoObj.getParserForType();

			ProtoEnum protoEnum = maps.get(parser);

			if(protoEnum != null)
			{

				return protoEnum;

			}

			String simpleName = protoObj.getClass().getSimpleName()
					.toUpperCase();

			for(ProtoEnum tempEnum:ProtoEnum.values())
			{

				if(simpleName.equals(tempEnum.toString()))
				{

					maps.put(parser,tempEnum);

					return tempEnum;

				}

			}

			return null;

		}

	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg,
                      ChannelPromise promise) throws Exception
	{

		try
		{
			if(msg instanceof GeneratedMessage)
			{
				GeneratedMessage generatedMsg = (GeneratedMessage)msg;
				writeImpl(ctx,promise,generatedMsg);
			}
			else if(msg instanceof Builder)
			{
				Builder<?> builder = (Builder<?>)msg;
				GeneratedMessage generatedMsg = (GeneratedMessage)builder
						.build();
				writeImpl(ctx,promise,generatedMsg);
			}
			else if(msg instanceof byte[])
			{
				assert false:"error: server do not provide byte proto currently";
				ByteBuf finalBuf = Unpooled.wrappedBuffer((byte[])msg);
				ProtoEnum protoEnum = ProtoEnum
						.getByIndex(finalBuf.getShort(4));
				writeBuffer(protoEnum,ctx,finalBuf,promise);
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
                             GeneratedMessage generatedMsg) throws Exception
	{
		EncoderMrg encoderMrg = localEncoderMrg.get();
		ProtoEnum protoEnum = encoderMrg.get(generatedMsg);
		if(protoEnum == null){
			logger.error(String.format("%s proto does not have a proto enum" ,generatedMsg.getClass().getName()));
			return;
		}
		// 协议枚举的名字不要过长好不好
		assert protoEnum.toString().length() <= 127:"error: proto enum too large " + protoEnum.toString();
		// proto对象序列化之后的协议长度
		int protoLength = generatedMsg.getSerializedSize();
		// 最后经过序列化的协议中包头前四个字节所标识的协议长度,其中1表示的是协议枚举对应字符串的长度,用一个字节来表示
		int finalProtoLen = 0;
		ByteBuf byteBuf = null;
		if(AnyUtils.IS_IN_DEBUG)
		{
			finalProtoLen = protoLength + 1 + protoEnum.toString().length();
			byteBuf = ctx.alloc().buffer(finalProtoLen + 4,finalProtoLen + 4);

			// 将协议的长度写入进来
			byteBuf.writeInt(finalProtoLen);
			// 协议号对应字符串的长度
			byteBuf.writeByte(protoEnum.toString().length());
			// 协议号对应的字符串
			byteBuf.writeBytes(protoEnum.toString().getBytes("UTF-8"));
		}
		else
		{
			finalProtoLen = protoLength + 4+4;
			byteBuf = ctx.alloc().buffer(finalProtoLen+4,finalProtoLen+4);

			// 将协议的长度写入进来
			byteBuf.writeInt(finalProtoLen);
			byteBuf.writeInt(protoEnum.ordinal());
			byteBuf.writeInt(sequence);
			++sequence;
		}
		try(ByteBufOutputStream out = new ByteBufOutputStream(byteBuf))
		{
			generatedMsg.writeTo(out);
			writeBuffer(protoEnum,ctx,out.buffer(),promise);
		}

	}

	protected void writeBuffer(ProtoEnum protoEnum, ChannelHandlerContext ctx,
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
