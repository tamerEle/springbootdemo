package com.zjpavt.socket.netty;

import com.zjpavt.socket.device.connect.DeviceConnectManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ServerHandler extends ChannelInboundHandlerAdapter implements SocketServer.NewSocketListener{
    private Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private static Map<String,Channel> channelMap = new ConcurrentHashMap<String,Channel>();
    public ServerHandler() {
    }
    @Autowired
    private DeviceConnectManager deviceConnectManager ;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("x".getBytes()));
        //list.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //list.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, "utf-8");
        log.info("channelRead: " + ctx.channel().id() + ctx.channel() + " receive message =" + request);
        if(request.contains("Device ID")){
            channelMap.put(request,ctx.channel());
            String sendMsg = "new channel join " + request + "; channel map size=" +channelMap.size() + "; ";
            //log.debug(sendMsg);
            //sendMsgAllActive(sendMsg);
            sendMsg(ctx.channel(),sendMsg);
        }else if(request.contains("heart")){
            sendMsg(ctx.channel(),"heart");
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
        String patternString = "[\\s\\S]*(远程主机强迫关闭了一个现有的连接。|你的主机中的软件中止了一个已建立的连接)[\\s\\S]*";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(cause.getMessage());
        if (pattern.matcher(cause.getMessage()).matches()) {
            log.debug("exception: " +ctx.channel().remoteAddress().toString()+  " disconnected");
            return;
        }
        cause.printStackTrace();
        //log.error(cause.getMessage() + "---" + ctx.channel());

    }
    private void sendMsg(Channel channel, String message){
        channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
        log.debug(channel + " send message =" +message);
    }
    private void sendMsgAllActive(String message){
        for(Channel channel : ServerHandler.channelMap.values()){
            sendMsg(channel,message);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        channelMap.values().remove(ctx.channel());
         sendMsg(ctx.channel(), String.valueOf(channelMap.size()));
        log.warn("channel inactive " + ctx.channel());
    }


    @Override
    public void newSocketJoin(Channel channel, String deviceID) {
        //SocketServer.
    }

    @Override
    public void socketDisconnect(Channel channel) {

    }


}
