package com.zjpavt.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ClientHandler extends ChannelInboundHandlerAdapter{
	private static ScheduledExecutorService scheduledExecutorService =
			new ScheduledThreadPoolExecutor(5,new DefaultThreadFactory("heart"));

	@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
	        try {
	            ByteBuf buf = (ByteBuf) msg;
	            byte[] data = new byte[buf.readableBytes()];  
	            buf.readBytes(data);
	            String strData = new String(data).trim();
	            System.out.println(ctx.channel().config() + "Client recive message is:" + strData);
	            if ("x".equals(strData)) {
	            	ctx.writeAndFlush(Unpooled.copiedBuffer(("Device ID:" + new Random().nextInt()).getBytes()));
				}
				scheduledExecutorService.scheduleAtFixedRate(()->{
					//sendMsg(ctx.channel(),"heart");
				},5,6, TimeUnit.SECONDS);
	        } finally {
	            ReferenceCountUtil.release(msg);  
	            //ctx.close();
	        }  
	    }  
	  
	  
	    @Override  
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
	        cause.printStackTrace();  
	        ctx.close();  
	    }
	    private void sendMsg(Channel channel,String msg){
	 		channel.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
		}
}
