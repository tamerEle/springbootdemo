package com.zjpavt.socket.netty.Client;

import com.zjpavt.util.ConfigUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author zyc
 */
public class Client implements Runnable {
	private static EventLoopGroup BOSS_GROUP= new NioEventLoopGroup();
	private static void runClient() {
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(BOSS_GROUP)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
			 @Override
			 protected void initChannel(SocketChannel socketChannel){
				 socketChannel.pipeline().addLast(new ClientHandler());
			 }
			});
			bootstrap.connect(ConfigUtil.SOCKET_CONNECT_SERVER_HOST, ConfigUtil.SOCKET_CONNECT_SERVER_PORT).sync();
            boolean flag = true;
            while(flag){
                bootstrap.connect(ConfigUtil.SOCKET_CONNECT_SERVER_HOST, ConfigUtil.SOCKET_CONNECT_SERVER_PORT).sync();
                Thread.sleep(10);
            }
    	}catch(Exception e) {
    	e.printStackTrace();
    	} 
    }
	public void close(){
		if (BOSS_GROUP != null){
			BOSS_GROUP.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
	    runClient();
	}

    @Override
    public void run() {
		runClient();
    }
}
