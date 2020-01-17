package com.zjzyc.socket.nettyio;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.UUID;

public class MessageEventHandler {
    @Autowired
    private SocketIOServer server;

    private static final Logger log = LoggerFactory.getLogger(MessageEventHandler.class);

    public SocketIOServer getServer() {
        return server;
    }

    public MessageEventHandler() {
        log.info("getServer");
    }

    public MessageEventHandler(SocketIOServer server) {
        log.info("getServer");
        this.server = server;
    }

    //添加connect事件，当客户端发起连接时调用，本文中将clientid与sessionid存入数据库
    //方便后面发送消息时查找到对应的目标client,
    @OnConnect
    public void onConnect(SocketIOClient client) {
        log.info("connect 已连接");
        /*String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
        ClientInfo clientInfo = clientInfoRepository.findClientByclientid(clientId);
        if (clientInfo != null)
        {
            Date nowTime = new Date(System.currentTimeMillis());
            clientInfo.setConnected((short)1);
            clientInfo.setMostsignbits(client.getSessionId().getMostSignificantBits());
            clientInfo.setLeastsignbits(client.getSessionId().getLeastSignificantBits());
            clientInfo.setLastconnecteddate(nowTime);
            clientInfoRepository.save(clientInfo);
        }*/
    }

    //添加@OnDisconnect事件，客户端断开连接时调用，刷新客户端信息
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        log.info("disconnect 已断开");
       /* String clientId = client.getHandshakeData().getSingleUrlParam("clientid");
        ClientInfo clientInfo = clientInfoRepository.findClientByclientid(clientId);
        if (clientInfo != null)
        {
            clientInfo.setConnected((short)0);
            clientInfo.setMostsignbits(null);
            clientInfo.setLeastsignbits(null);
            clientInfoRepository.save(clientInfo);
        }*/
    }

    //消息接收入口，当接收到消息后，查找发送目标客户端，并且向该客户端发送消息，且给自己发送消息
    @OnEvent(value = "messageevent")
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        log.info("收到消息");
        /*String targetClientId = data.getTargetClientId();
        ClientInfo clientInfo = clientInfoRepository.findClientByclientid(targetClientId);
        if (clientInfo != null && clientInfo.getConnected() != 0)
        {
            UUID uuid = new UUID(clientInfo.getMostsignbits(), clientInfo.getLeastsignbits());
            System.out.println(uuid.toString());
            MessageInfo sendData = new MessageInfo();
            sendData.setSourceClientId(data.getSourceClientId());
            sendData.setTargetClientId(data.getTargetClientId());
            sendData.setMsgType("chat");
            sendData.setMsgContent(data.getMsgContent());
            client.sendEvent("messageevent", sendData);
            server.getClient(uuid).sendEvent("messageevent", sendData);
        }*/

    }

    public void sendBuyLogEvent(ArrayList<UUID> listClient) {   //这里就是向客户端推消息了
        String dateTime = new DateTime().toString("hh:mm:ss");

        for (UUID clientId : listClient) {
            if (server.getClient(clientId) == null) {
                continue;
            }
            server.getClient(clientId).sendEvent("enewbuy", dateTime, 1);
        }
    }
}