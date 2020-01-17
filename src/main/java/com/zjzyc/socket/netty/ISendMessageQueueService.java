package com.zjzyc.socket.netty;

import io.netty.channel.Channel;

public interface ISendMessageQueueService {
    /**
     * send message in the channel
     * @param channel   channel
     * @param message   message to send
     */
    void sendMessage(Channel channel, String message);
}
