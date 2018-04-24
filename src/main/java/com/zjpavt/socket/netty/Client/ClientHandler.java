package com.zjpavt.socket.netty.Client;

import com.zjpavt.util.ConfigUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * @author zyc
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(ClientHandler.class);

	/*@Autowired
	private ISendMessageQueueService sendMessageQueueService; TODO*/

	@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	        try {

				String receiveMessage = receiveMessage((ByteBuf) msg, ctx.channel());
	            if (ConfigUtil.SOCKET_CONNECT_DEVICE_ID_COMMAND.equals(receiveMessage)) {
	            	sendMsg(ctx.channel(),"Device ID:" + new Random().nextInt());
				}
	        } finally {
	            ReferenceCountUtil.release(msg);
	            //ctx.close();
	        }  
	    }  
	  
	  
	    @Override  
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	        cause.printStackTrace();  
	        ctx.close();  
	    }
	private void sendMsg(Channel channel, String message){
		channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
		log.debug(channel + " send message =" +message);
	}
	private String receiveMessage(ByteBuf buf, Channel channel) throws UnsupportedEncodingException {
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);
		String request = new String(data, ConfigUtil.SOCKET_CONNECT_CHARSET);
		buf.release();
		log.info("channelRead: " + channel + " receive message =" + request);
		return request;
	}


}
