package com.zjpavt.socket.hfNetty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class Server {
	public static final String HOST = "127.0.0.1";
	public static final int PORT = 8081;
	public static final int PORT2 = 8082;
	public static final int CLIENT_PORT = 8081;
	public static final int CLIENT_PORT2 = 8082;

	 private int port ;

	public Server(int port) {
		//super();
		this.port = port;
	}
	public static void main(String[] args) {
		System.out.println("开始启动...");
		new Server(PORT).run();
	}
	public void run(){
		EventLoopGroup loopone=new NioEventLoopGroup(1,new DefaultThreadFactory("one"));
		EventLoopGroup looptwo=new NioEventLoopGroup();
		ServerBootstrap bootstrap=new ServerBootstrap();
		try {
			bootstrap.group(loopone, looptwo).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel ch) throws Exception {
					ch.pipeline().addLast(new ServerHandler());
				}
			});
			ChannelFuture cf=bootstrap.bind(port).sync();
			cf.channel().closeFuture().sync();//
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			loopone.shutdownGracefully();
			looptwo.shutdownGracefully();
		}
	}
}  


	
