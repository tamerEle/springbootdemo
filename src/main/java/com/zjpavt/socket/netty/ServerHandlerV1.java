package com.zjpavt.socket.netty;

import com.zjpavt.socket.device.connect.DeviceConnect;
import com.zjpavt.socket.device.connect.DeviceConnectManager;
import com.zjpavt.socket.device.connect.IDeviceConnectManager;
import com.zjpavt.util.ConfigUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@ChannelHandler.Sharable
public class ServerHandlerV1 extends ChannelInboundHandlerAdapter implements SocketServer.NewSocketListener{
    private Logger log = LoggerFactory.getLogger(ServerHandler.class);
    private Map<Channel, DeviceConnect> channelDeviceConnectMap = new ConcurrentHashMap<>();
    private static final String CHARSET = "UTF-8";
    final Pattern deviceIDPattern = Pattern.compile(ConfigUtil.SOCKET_CONNECT_DEVICE_ID + "([\\s\\S]*)");
    public ServerHandlerV1() {
    }

    @Autowired
    private IDeviceConnectManager deviceConnectManager ;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        //ctx.writeAndFlush(Unpooled.copiedBuffer("x".getBytes()));
        //list.add(ctx.channel());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        //list.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String receiveMsg = receiveMessage((ByteBuf) msg, ctx.channel());
        Matcher deviceIDMatcher = this.deviceIDPattern.matcher(receiveMsg);
        if(deviceIDMatcher.matches()){
            String deviceSerial = deviceIDMatcher.group(0);
            UUID deviceID = UUID.randomUUID();//TODO the deviceID is not request.it is from database.
            UUID connectID = this.channelDeviceConnectMap.get(ctx.channel()).getConnectID();
            DeviceConnect deviceConnect = deviceConnectManager.getByConnectID(connectID);
            this.deviceConnectManager.login(deviceConnect, deviceID, deviceSerial);
        }else if(receiveMsg.contains("heart")){
           // sendMsg(ctx.channel(),"heart");
        }
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
        /*for(Channel channel : channelMap.values()){
            sendMsg(channel,message);
        }*/
    }
    private void askForLogin(Channel channel) {
        sendMsg(channel,"x");
    }
    private String receiveMessage(ByteBuf msg, Channel channel) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf) msg;
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        String request = new String(data, CHARSET);
        log.info("channelRead: " + channel + " receive message =" + request);
        return request;
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DeviceConnect deviceConnect = deviceConnectManager.addConnect(ctx.channel());
        channelDeviceConnectMap.put(ctx.channel(), deviceConnect);
        askForLogin(ctx.channel());
        super.channelActive(ctx);
        log.debug("channel active " + ctx.channel() + " size=" + deviceConnectManager.countTotalConnect());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DeviceConnect deviceConnect = channelDeviceConnectMap.get(ctx.channel());
        deviceConnectManager.logout(deviceConnect);
        deviceConnectManager.removeConnect(deviceConnect.getConnectID());
        /*sendMsg(ctx.channel(), String.valueOf(deviceConnectManager.size()));*/
        log.debug("channel inactive " + ctx.channel() + " size=" + deviceConnectManager.countTotalConnect());
    }


    @Override
    public void newSocketJoin(Channel channel, String deviceID) {
        //SocketServer.
    }

    @Override
    public void socketDisconnect(Channel channel) {

    }


}
