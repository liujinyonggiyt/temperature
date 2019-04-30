package com.ljy.mrg;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ljy.ProtoEnum;
import com.ljy.misc.msg.ClientProtoHandler;
import com.ljy.misc.session.ClientSession;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author liujinyong
 */
public class ClientProtoHandlerMrg{
	private final Logger logger = LoggerFactory.getLogger(ClientProtoHandlerMrg.class);

	private final Map<ProtoEnum,ClientProtoHandler> clientProtoHandlers = new EnumMap<>(
			ProtoEnum.class);
	/**
	 * 客户端的会话管理器
	 */
	private final ClientSessionMrg clientSessionMrg;
	@Inject
	public ClientProtoHandlerMrg(ClientSessionMrg clientSessionMrg)
	{
		this.clientSessionMrg = clientSessionMrg;
	}

	/**
	 * 处理客户端发送过来的协议
	 *
	 * @param protoEnum
	 *            协议枚举
	 * @param channel
	 *            连接对象
	 * @param proto
	 *            协议对象
	 */
	public final void handleClientProto(ProtoEnum protoEnum,Channel channel,
			Object proto) throws Exception
	{

		ClientProtoHandler clientProtoHandler = clientProtoHandlers
				.get(protoEnum);
//        logger.info(protoEnum.toString());
		if(null == clientProtoHandler)
		{
			logger.error("error: a illegal channel send a not exist proto to scene server , kick it out proto enum is: {}",protoEnum.toString());
			return;
		}
		//
		ClientSession clientSession = clientSessionMrg.getSessionByChannel(channel);
		if(null != clientSession)
		{
			clientProtoHandler.handle(channel,clientSession,proto);
		}

	}

	/**
	 * 注册一个客户端协议处理handler接口
	 *
	 * @param protoEnum
	 *            协议枚举
	 * @param clientProtoHandler
	 *            协议处理handler
	 */
	public void registerClient(ProtoEnum protoEnum,
			ClientProtoHandler clientProtoHandler)
	{
		Preconditions
				.checkArgument(!clientProtoHandlers.containsKey(protoEnum), protoEnum.toString());
		clientProtoHandlers.put(protoEnum,clientProtoHandler);
	}


}
