package com.ljy.mrg;

import com.google.inject.Inject;
import com.ljy.misc.net.ConnectionInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Author:liujinyong
 * Date:2019/2/21
 * Time:11:59
 */
public class AcceptorMrg {
    @Inject
    public AcceptorMrg() {
    }

    public ConnectionInfo acceptRange(String acceptHost, int fromPort, int toPort, ChannelInitializer<SocketChannel> init, EventLoopGroup boss, EventLoopGroup worker) {
        for(int port = fromPort; port <= toPort; ++port) {
            ConnectionInfo connectionInfo = this.accept(acceptHost, port, init, boss, worker);
            if (null != connectionInfo) {
                return connectionInfo;
            }
        }

        return null;
    }

    public ConnectionInfo accept(String acceptHost, int port, ChannelInitializer<SocketChannel> init, EventLoopGroup boss, EventLoopGroup worker) {
        ChannelFuture cf = null;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_REUSEADDR, true);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 400);
        serverBootstrap.option(ChannelOption.SO_RCVBUF, 8196);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, false);
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_SNDBUF, 2048);
        serverBootstrap.childOption(ChannelOption.SO_LINGER, 0);
        serverBootstrap.childHandler(init);

        try {
            cf = serverBootstrap.bind(acceptHost, port);
            Channel channel = cf.sync().channel();
            InetSocketAddress socketAddress = (InetSocketAddress)channel.localAddress();
            return new ConnectionInfo(socketAddress.getHostString(), socketAddress.getPort());
        } catch (Exception var10) {
            this.cleanFuture(cf);
            return null;
        }
    }

    public ConnectionInfo acceptRange(int fromPort, int toPort, ChannelInitializer<SocketChannel> init, EventLoopGroup boss, EventLoopGroup worker) {
        return this.acceptRange("0.0.0.0", fromPort, toPort, init, boss, worker);
    }

    private void cleanFuture(ChannelFuture cf) {
        try {
            if (cf == null) {
                return;
            }

            cf.cancel(true);
            cf.channel().close();
        } catch (Exception var3) {
            ;
        }

    }

    public ChannelFuture connect(String host, int port, EventLoopGroup worker, ChannelInitializer<SocketChannel> init) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(worker);
        bootstrap.handler(init);
        ((Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)bootstrap.option(ChannelOption.SO_KEEPALIVE, false)).option(ChannelOption.TCP_NODELAY, true)).option(ChannelOption.SO_RCVBUF, 8196)).option(ChannelOption.SO_SNDBUF, 2048)).option(ChannelOption.SO_LINGER, 0);
        ChannelFuture future = bootstrap.connect(host, port);
        return future;
    }
}
