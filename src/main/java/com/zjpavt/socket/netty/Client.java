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

import java.util.concurrent.*;

public class Client implements Runnable {
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
            ChannelFuture cf2=bootstrap.connect(Server.HOST, Server.CLIENT_PORT2).sync();
            boolean flag = true;
            while(flag){
                ChannelFuture cf3=bootstrap.connect(Server.HOST, Server.CLIENT_PORT2).sync();
                Thread.sleep(10000);
            }
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
	    runClient();
		/*ScheduledExecutorService schedule=new ScheduledThreadPoolExecutor(5,new DefaultThreadFactory("send request"));
	    schedule.scheduleAtFixedRate(()->{
	    	 	try {
	    	 		System.out.println(System.currentTimeMillis());
					runClient();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}, 0, 1, TimeUnit.SECONDS);*/


		//ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5,1,TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(1));

		//runClient();
      /*  for(int i=0;i<100;i++){
            new Thread((Runnable) new Client()).run();
            Thread.sleep(1000);
        }*/
	}

    @Override
    public void run() {
        try {
            runClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
