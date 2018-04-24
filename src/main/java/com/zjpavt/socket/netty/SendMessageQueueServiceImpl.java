package com.zjpavt.socket.netty;

import com.zjpavt.util.ConfigUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @author zyc
 */
@Slf4j
@Service
public class SendMessageQueueServiceImpl implements ISendMessageQueueService {
    private final BlockingQueue<Task> messageQueue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(5,new DefaultThreadFactory("messageQueue"));
    @Override
    public void sendMessage(Channel channel, String message) {
        Task sendMessageTask = new Task(channel, message);
        messageQueue.add(sendMessageTask);
        //scheduledExecutorService.schedule(()->channel.writeAndFlush("1"),1,TimeUnit.SECONDS,)
        scheduledExecutorService.schedule(new SendTask(), ConfigUtil.SOCKET_CONNECT_SEND_PERIOD, TimeUnit.MILLISECONDS);
    }

    public class SendTask implements Runnable {
        SendTask() {
            super();
        }
        @Override
        public void run() {
            Task task2Send = null;
            try {

                synchronized (messageQueue){
                    //maybe when the queue is empty after judge. so add synchronized
                    if (!messageQueue.isEmpty()) {
                        task2Send = messageQueue.take();
                    } else {
                        log.warn("the message is empty when need to send message!");
                        return;
                    }
                }
                sendMsg(task2Send.getChannel(), task2Send.getMsg());
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (task2Send != null) {
                    messageQueue.add(task2Send);
                }
            }
        }
    }

    public class Task {
        private Channel channel;
        private String msg;
        public Task(Channel channel, String msg) {
            this.channel = channel;
            this.msg = msg;
        }

        public Channel getChannel() {
            return channel;
        }

        public String getMsg() {
            return msg;
        }
    }
    private void sendMsg(Channel channel, String message){
        if (!channel.isActive()) {
            log.warn("the channel is inactive when the msg want to send.");
            return;
        }
        channel.writeAndFlush(Unpooled.copiedBuffer(message.getBytes()));
        log.debug(channel + " send message =" + message + " " +messageQueue.size());
    }
}
