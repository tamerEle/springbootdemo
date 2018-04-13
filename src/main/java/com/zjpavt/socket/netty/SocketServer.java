package com.zjpavt.socket.netty;

import com.zjpavt.socket.hfNetty.Server;
import com.zjpavt.springbootdemo.SpringbootNettyDemoApplication;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SocketServer {
    private static Logger log = LoggerFactory.getLogger(SocketServer.class);
    public static void main(String[] args){
        System.out.println("SocketServer开始启动...");
        newServer();
    }

    private ServerHandler serverHandler;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    public SocketServer(ServerHandler serverHandler){
        this.serverHandler = serverHandler;
    }

    public SocketServer(){
    }
    public void start(){

        log.info("SocketServer 启动中..." + this.serverHandler);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
               /* .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {

                        channel.pipeline().addLast(this.serverHandler);
                        //channel.pipeline().addLast(new ServerHandler());
                    }
                });*/
               .childHandler(new ServerInitializer(/*this.serverHandler*/));
        try {
            log.info("SocketServer 绑定端口");
            ChannelFuture channelFuture = serverBootstrap.bind(Server.PORT2).sync();
            //服务器断开连接才向下继续执行
            //channelFuture.channel().closeFuture().sync();
            log.info("SocketServer 启动成功");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void close(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


    public static void newServer(){
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer());
            try {
                ChannelFuture channelFuture = serverBootstrap.bind(Server.PORT2).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
    }

}
