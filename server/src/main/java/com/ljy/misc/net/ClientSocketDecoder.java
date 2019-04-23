package com.ljy.misc.net;

import com.ljy.misc.GlobalQueue;
import com.ljy.misc.LogicEvent;
import com.ljy.misc.LogicEventType;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lishile
 */
public class ClientSocketDecoder extends AppSocketFrameDecoder
{
	private static final Logger logger = LoggerFactory
			.getLogger(ClientSocketDecoder.class);
	private static final int MAX_CLIENT_PACKAGE_LENGTH = 8192;
	
	public ClientSocketDecoder()
	{
		super(MAX_CLIENT_PACKAGE_LENGTH);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelActive(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception
	{
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelInactive(ctx);
		// 通知服务器客户端关闭了socket连接
		ensuerLogicQueue();
		long sequence = logicQueue.next();
		try
		{
			LogicEvent logicEvent = logicQueue.get(sequence);
			logicEvent.setChannel(ctx.channel());
			logicEvent.setLogicEventType(LogicEventType.LOGIC_CLIENT_DISCONNECT);
		}
		finally
		{
			logicQueue.publish(sequence);
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx)
	{
		super.channelRegistered(ctx);
		
		// 通知游戏服务器客户端连接了过来
		RingBuffer<LogicEvent> logicEventQueue = GlobalQueue.logicQueue;
		long sequence = logicEventQueue.next();
		try
		{
			LogicEvent event = logicEventQueue.get(sequence);
			event.setLogicEventType(LogicEventType.LOGIC_CLIENT_REGISTING);
			event.setChannel(ctx.channel());
		}
		finally
		{
			logicEventQueue.publish(sequence);
		}
	}
	@Override
	protected LogicEventType getEventType1() {
		return LogicEventType.LOGIC_CLIENT_PROTO_COMMING_EVENT;
	}

}
