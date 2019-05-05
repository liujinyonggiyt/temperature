package com.ljy.misc.net;

import com.ljy.misc.GlobalQueue;
import com.ljy.misc.LogicEvent;
import com.ljy.misc.LogicEventType;
import com.ljy.misc.msg.ClientRequest;
import com.ljy.misc.msg.RequestMsg;
import com.lmax.disruptor.RingBuffer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DefaultSocketChannelConfig;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author lishile
 *
 */
public abstract class AppSocketFrameDecoder extends
        LengthFieldBasedFrameDecoder
{
	private static final Logger logger = LoggerFactory
			.getLogger(AppSocketFrameDecoder.class);

	protected volatile RingBuffer<LogicEvent> logicQueue = null;

	public AppSocketFrameDecoder(int maxFrameLength)
	{
		super(maxFrameLength,0,4,0,4);
	}
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception
	{
		// 走到这里的原因有多种
		logger.debug("my be remove peer close process maully");
	}

	@Override
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer,
                                   int index, int length)
	{
		ensuerLogicQueue(); 
		ByteBuf rtBuf = buffer.slice(index,length);

		try
		{
			byte[] bytes = new byte[rtBuf.readableBytes()];
			rtBuf.readBytes(bytes);

			RequestMsg clientRequest = new ClientRequest(bytes);
			long sequence = logicQueue.next();
			try
			{
				LogicEvent logicEvent = logicQueue.get(sequence);
				logicEvent.setLogicEventType(getEventType1());
				logicEvent.setChannel(ctx.channel());
				logicEvent.setParamA(clientRequest);
			}
			finally
			{
				logicQueue.publish(sequence);
			}
		}
		catch(Exception e)
		{
			logger.error("",e);
		}
		return Unpooled.EMPTY_BUFFER;
	}
	
	protected abstract LogicEventType getEventType1();

	protected void ensuerLogicQueue()
	{
		if(null == logicQueue)
		{

			logicQueue = GlobalQueue.logicQueue;

		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx)
	{

		ChannelConfig config = ctx.channel().config();

		DefaultSocketChannelConfig socketConfig = (DefaultSocketChannelConfig)config;

		socketConfig.setPerformancePreferences(0,1,2);

		ctx.fireChannelRegistered();
	}

}
