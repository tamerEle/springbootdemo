package com.zjpavt.springbootdemo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zyc
 */


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.zjpavt.httprange","com.zjpavt.socket.netty","com.zjpavt.socket.device.connect"})
@Slf4j
public class SpringbootNettyDemoApplication /*implements CommandLineRunner */{
    /**
     *
     * @param args parameter to start
     */
    public static void main(String[] args){
        SpringApplication.run(SpringbootNettyDemoApplication.class,args);
        log.info("spring started");
    }
}
