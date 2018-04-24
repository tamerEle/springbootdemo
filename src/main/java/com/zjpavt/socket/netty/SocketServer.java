package com.zjpavt.socket.netty;

import com.zjpavt.util.ConfigUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author zyc
 */

@Service
@Slf4j
public class SocketServer {
    private static final int SERVER_PORT = ConfigUtil.SOCKET_CONNECT_SERVER_PORT;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    private ChannelInboundHandlerAdapter serverHandler;

    @Autowired
    public SocketServer(ChannelInboundHandlerAdapter serverHandler){
        Assert.notNull(serverHandler,"serverHandler most not be null");
    }
    void start(){
        log.info("SocketServer 启动中..." + this);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel){
                        channel.pipeline().addLast(serverHandler);
                    }
                });
        try {
            log.info("SocketServer 绑定端口");
            serverBootstrap.bind(SERVER_PORT).sync();
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

    public static void main(String[] args){
        System.out.println("SocketServer开始启动...");
    }
}
