package com.zjpavt.springbootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjpavt.socket.netty","com.zjpavt.socket.device.connect"})
public class SpringbootNettyDemoApplication /*implements CommandLineRunner */{
    public static final Logger log = LoggerFactory.getLogger(SpringbootNettyDemoApplication.class);
    public static void main(String[] args){
        SpringApplication.run(SpringbootNettyDemoApplication.class,args);
        log.info("spring started");
    }
}
