package com.zjpavt.socket.netty;

import com.zjpavt.socket.device.connect.DeviceConnectManager;
import com.zjpavt.socket.hfNetty.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SocketServer {
    private static Logger log = LoggerFactory.getLogger(SocketServer.class);
    private static final List<SocketConnectedBean> socket =
            Collections.synchronizedList(new ArrayList< SocketConnectedBean >());
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    @Autowired
    private ChannelInboundHandlerAdapter serverHandler;

    public SocketServer(){
    }
    public void start(){
        log.info("SocketServer 启动中...");
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        //Server Handler 是一个多例对象
                        if (false) {
                            //channel.pipeline().addLast(new ServerHandler());
                        } else {
                            channel.pipeline().addLast(serverHandler);
                        }
                    }
                });
        try {
            log.info("SocketServer 绑定端口");
            ChannelFuture channelFuture = serverBootstrap.bind(Server.PORT2).sync();
            //服务器断开连接才向下继续执行
            //channelFuture.channel().closeFuture().sync();
            log.info("SocketServer 启动成功");
        } catch (InterruptedException e) {
            log.error("SocketServer 没有启动");
            e.printStackTrace();
        }
    }
    public void close(){
        if (bossGroup != null){
            bossGroup.shutdownGracefully();
        }
        if(workGroup  != null){
            workGroup.shutdownGracefully();
        }
    }

    @PostConstruct
    public void newServer(){
            this.start();
    }

    @PreDestroy
    public void deleteServer(){

    }

    public interface NewSocketListener{
        public void newSocketJoin(Channel channel,String deviceID);
        public void socketDisconnect(Channel channel);
    }
    public static void main(String[] args){
        System.out.println("SocketServer开始启动...");
        //newServer();
    }
}
