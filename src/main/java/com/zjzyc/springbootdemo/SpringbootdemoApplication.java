package com.zjzyc.springbootdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

//@EnableAutoConfiguration
//@SpringBootApplication
public class SpringbootdemoApplication {
	private static final Logger log = LoggerFactory.getLogger(SpringbootdemoApplication.class);
	private static final String HOST = "127.0.0.1";
	private static final int PORT = 9092;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootdemoApplication.class, args);
	}
/*
	@Bean
	public SocketIOServer socketIOServer() {
		com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
		String os = System.getProperty("os.name");
		System.out.println("this is " + os);
		config.setHostname(HOST);
		config.setPort(PORT);
        *//*config.setAuthorizationListener(new AuthorizationListener() {//类似过滤器
            @Override
            public boolean isAuthorized(HandshakeData data) {
                //http://localhost:8081?username=test&password=test
                //例如果使用上面的链接进行connect，可以使用如下代码获取用户密码信息，本文不做身份验证
                // String username = data.getSingleUrlParam("username");
                // String password = data.getSingleUrlParam("password");
                return true;
            }
        });*//*
		final SocketIOServer server = new SocketIOServer(config);
		return server;
	}

	@Bean
	public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketServer) {
		log.info("bean-------------------------------------");
		return new SpringAnnotationScanner(socketServer);
	}*/


}
