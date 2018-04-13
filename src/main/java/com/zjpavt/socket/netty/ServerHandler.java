package com.zjpavt.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private List<Channel> list = new ArrayList<Channel>();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("x".getBytes()));
        list.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        list.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        log.info("channelRead" + ctx.name() + ctx.channel() + " receive message =" + request);
        if(request.contains("drivce ID:")){
            System.out.println("add drivce " + request);
            for(Channel channel :this.list){
                String sendMsg = "new channel join " + request + " total=" + list.size();
                sendMsg(channel,sendMsg);
                /*channel.writeAndFlush(Unpooled.copiedBuffer(
                        ("new channel join " + request + " total=" + list.size()).getBytes())
                );*/
            }
        }
        /*
        System.out.println("Server: " + request);
        ctx.writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
        Thread.sleep(1000);
        ctx.writeAndFlush(Unpooled.copiedBuffer("msg before close".getBytes()));
        log.info("msg before close");
        ctx.channel().closeFuture();
        ctx.writeAndFlush("msg after close");
        log.info("msg after close");*/

        //ReferenceCountUtil.release(msg);//
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage() + "---" + ctx.channel());

    }
    private void sendMsg(Channel channel, String message){
        channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
        log.debug(channel + " send message =" +message);
    }
/*  @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println("Client：");
            ByteBuf buf = (ByteBuf) msg;
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            System.out.println("Client：" + new String(data, "utf-8").trim());
        } finally {
            //ReferenceCountUtil.release(msg);
            //ctx.close();
        }
    }*/
}
