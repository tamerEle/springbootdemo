package com.zjpavt.socket.hfNetty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	 private int port ;

	public Server(int port) {
		//super();
		this.port = port;
	}
	public static void main(String[] args) {
		System.out.println("��������...");
		new Server(8084).run();
	}
	public void run(){
		//���������߳���
		EventLoopGroup loopone=new NioEventLoopGroup();
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
			cf.channel().closeFuture().sync();//������ǰ�̣߳��ȴ��ͻ������ӵ�����
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			loopone.shutdownGracefully();
			looptwo.shutdownGracefully();
		}
	}
}  


	
