package com.zjpavt.socket.netty;

import com.zjpavt.socket.hfNetty.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {
	private static void runClient() throws InterruptedException {  
		EventLoopGroup looptwo=new NioEventLoopGroup();
		Bootstrap bootstrap=new Bootstrap();
		try {
			bootstrap.group(looptwo)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
			 @Override
			 protected void initChannel(SocketChannel socketChannel) throws Exception {
				 socketChannel.pipeline().addLast(new ClientHandler());
			 }
			});
			ChannelFuture cf=bootstrap.connect(Server.HOST, Server.CLIENT_PORT2).sync();
			cf.channel().closeFuture().sync();
			looptwo.shutdownGracefully();
    	}catch(Exception e) {
    	e.printStackTrace();
    	} 
		finally {
			looptwo.shutdownGracefully();
		}
    }
	public static void main(String[] args) throws InterruptedException {
		ScheduledExecutorService schedule=new ScheduledThreadPoolExecutor(5,new DefaultThreadFactory("send request"));
	    schedule.scheduleAtFixedRate(()->{
	    	 	try {
					runClient();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}, 0, 10, TimeUnit.SECONDS);
		//runClient();
	}
}
