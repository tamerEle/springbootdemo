package com.zjzyc.socket;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

/**
 *
 * @author zyc
 */

/*@Component
@Order(value=1)*/
public class MyCommandLineRunner implements CommandLineRunner {
    private final SocketIOServer server;

    @Autowired
    public MyCommandLineRunner(SocketIOServer server) {
        System.out.println(server.getConfiguration().getHostname());
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        server.start();
        System.out.println("socket.io启动成功！");
    }
}