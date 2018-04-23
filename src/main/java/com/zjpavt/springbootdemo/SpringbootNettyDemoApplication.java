package com.zjpavt.springbootdemo;

import com.zjpavt.socket.netty.ServerHandler;
import com.zjpavt.socket.netty.ServerInitializer;
import com.zjpavt.socket.netty.SocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.net.Socket;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjpavt.socket.netty","com.zjpavt.socket.device.connect"})
public class SpringbootNettyDemoApplication /*implements CommandLineRunner */{
    public static final Logger log = LoggerFactory.getLogger(SpringbootNettyDemoApplication.class);
    public static void main(String[] args){
        SpringApplication.run(SpringbootNettyDemoApplication.class,args);
        log.info("spring started");
    }

    @Bean
    public ServerHandler getServerHandler(){
        log.info("getServerHandler");
        return new ServerHandler();
    }


    @Bean
    public ServerInitializer getServerInitializer(){
        return new ServerInitializer();
    }
    public void run(String... args){
        log.info("server starting");
        //socketServer.start();
        log.info("server started");
    }
}
