package com.zjpavt.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelInboundHandlerAdapter{
	 @Override  
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
	        try {  
	            ByteBuf buf = (ByteBuf) msg;  
	            byte[] data = new byte[buf.readableBytes()];  
	            buf.readBytes(data);
	            String strData = new String(data).trim();
	            System.out.println(ctx.channel().config() + "Client recive message is:" + strData);
	            if ("x".equals(strData)) {
	            	ctx.writeAndFlush(Unpooled.copiedBuffer(("drivce ID:" + Math.random()).getBytes()));
				}
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
}
