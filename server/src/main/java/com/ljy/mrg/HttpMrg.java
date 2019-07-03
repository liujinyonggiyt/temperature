package com.ljy.mrg;

import com.google.inject.Inject;
import com.ljy.misc.msg.SystemTimeMrg;
import com.ljy.misc.net.http.ServerHttpChannelInitializer;
import com.ljy.misc.trigger.Timer;
import com.ljy.misc.trigger.TriggerSystem;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpMrg extends TriggerSystem {
    private static final Logger logger = LoggerFactory.getLogger(HttpMrg.class);
	private final HttpEventHandlerMrg httpEventHandlerMrg;
	
	private final Map<String,ChannelSession> channelIdAndChannel=new HashMap<>();
	
	private final SystemTimeMrg systemTimeMrg;
	@Inject
	public HttpMrg(AppUpdateMrg appUpdateMrg,
                   HttpEventHandlerMrg httpEventHandlerMrg, HttpEventQueueMrg httpEventQueueMrg,
                   EventGroupMrg eventGroupMrg,
                   SystemTimeMrg systemTimeMrg) throws InterruptedException{
		this.httpEventHandlerMrg=httpEventHandlerMrg;
		this.systemTimeMrg=systemTimeMrg;
		ServerHttpChannelInitializer init=new ServerHttpChannelInitializer(httpEventQueueMrg);
		int port = 10002;
		acceptService(port, 32*1024, 32*1024, "0.0.0.0", init, eventGroupMrg.getBossGroup(), eventGroupMrg.getWorkGroup());
		httpEventHandlerMrg.registerConnectHandler(this::onHttpChannelConnect);
		httpEventHandlerMrg.registerDisconnectHandler(this::onHttpChannelDisconnect);
		httpEventHandlerMrg.registerHandler("/checkUpdate", appUpdateMrg::checkUpdate);
		addTimer(new Timer(30000,Integer.MAX_VALUE,this::timerCheckChannelTimeout){});
        logger.info("http prot listen success!port:"+port);
	}
	
	private void onHttpChannelConnect(Channel channel){
		channelIdAndChannel.put(channel.id().asLongText(), new ChannelSession(channel, systemTimeMrg.getSysMillTime()));
	}
	
	private void onHttpChannelDisconnect(Channel channel){
		ChannelSession session=channelIdAndChannel.remove(channel.id().asLongText());
		if(session==null){
			return;
		}
		try{
			session.getChannel().close();
		}catch(Exception e){
			
		}
	}
	
	public void send(String channelId,Object proto){
		ChannelSession session=channelIdAndChannel.get(channelId);
		if(session==null){
			return;
		}
		session.getChannel().writeAndFlush(proto);
	}
	
	public void tick() throws Exception {
		httpEventHandlerMrg.tick();
		tickTrigger();
	}
	
	public final void acceptService(int port, int socketSendSize, int socketRecvSize, String Ip,
                                    ChannelInitializer<SocketChannel> initializer, EventLoopGroup bossGroup, EventLoopGroup workGroup) throws InterruptedException {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		serverBootstrap.option(ChannelOption.SO_REUSEADDR, Boolean.valueOf(true));
		serverBootstrap.option(ChannelOption.SO_BACKLOG, Integer.valueOf(400));
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(false));
		serverBootstrap.childOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
		serverBootstrap.childOption(ChannelOption.SO_RCVBUF, Integer.valueOf(socketRecvSize));
		serverBootstrap.childOption(ChannelOption.SO_SNDBUF, Integer.valueOf(socketSendSize));
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(false));
		serverBootstrap.childOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
		serverBootstrap.childOption(ChannelOption.SO_LINGER, Integer.valueOf(0));
		serverBootstrap.childHandler(initializer);
		serverBootstrap.bind(Ip, port).sync();
	}
	
	private void timerCheckChannelTimeout(Timer timer){
		Iterator<Entry<String, ChannelSession>> iter=channelIdAndChannel.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, ChannelSession> pair=iter.next();
			ChannelSession session=pair.getValue();
			if(session.getCreateMilltime()+60000<systemTimeMrg.getSysMillTime()){
				iter.remove();
				try{
					session.getChannel().close();
				}catch(Exception e){
					
				}
			}
		}
	}
	
	private static class ChannelSession{
		private final Channel channel;
		
		private final long createMilltime;

		public ChannelSession(Channel channel, long createMilltime) {
			super();
			this.channel = channel;
			this.createMilltime = createMilltime;
		}

		public Channel getChannel() {
			return channel;
		}

		public long getCreateMilltime() {
			return createMilltime;
		}
	}
}
