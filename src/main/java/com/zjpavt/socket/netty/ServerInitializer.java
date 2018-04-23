package com.zjpavt.socket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//@Service
public class ServerInitializer extends ChannelInitializer<Channel>{
    private Logger logger  = LoggerFactory.getLogger(ServerInitializer.class);
    @Override
    protected void initChannel(Channel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
       /* pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        pipeline.addLast(new LengthFieldPrepender(4));
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));*/
       try {
           if(false){//
               pipeline.addLast(serverHandler);
           }else {
             //  this.serverHandler = new ServerHandler();
               pipeline.addLast(serverHandler);
           }

           /**/
       }catch (NullPointerException e){
           logger.error("the value of ServerHandler is null");
           pipeline.addLast(new ServerHandler());
       }

        //socketChannel.pipeline().addLast(new ServerHandler());
    }

    private ServerHandler serverHandler;

    public ServerHandler getServerHandler() {
        return serverHandler;
    }
    public ServerInitializer(){

    }
    public ServerInitializer(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }
}
