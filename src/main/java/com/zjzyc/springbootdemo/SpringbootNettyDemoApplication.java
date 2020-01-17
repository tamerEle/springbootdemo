package com.zjzyc.springbootdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zyc
 */


@SpringBootApplication
@ComponentScan(basePackages = {"com.zjzyc.httpRange", "com.zjzyc.socket.netty", "com.zjzyc.socket.device.connect"})
@Slf4j
@RestController
public class SpringbootNettyDemoApplication /*implements CommandLineRunner */{
    /**
     *
     * @param args parameter to start
     */
    public static void main(String[] args){
        SpringApplication.run(SpringbootNettyDemoApplication.class,args);
        log.info("spring started");
    }
    @RequestMapping(value = "/hello")
    public String hello() {
        return "hello";
    }
}
