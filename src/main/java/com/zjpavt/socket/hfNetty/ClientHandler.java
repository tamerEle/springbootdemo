package com.zjpavt.socket.hfNetty;

import io.netty.buffer.ByteBuf;
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
	            System.out.println("Client��" + new String(data).trim());  
	        } finally {  
	            ReferenceCountUtil.release(msg);  
	            ctx.close();
	        }  
	    }  
	  
	  
	    @Override  
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
	        cause.printStackTrace();  
	        ctx.close();  
	    }  
}
