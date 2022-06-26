package com.zy.spring.mildware.mq.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 适用于响应时优先的同时对准确度要求不高的应用
 * 发送方不阻塞, 发送完即可响应给客户端
 */
public class ProducerAsync {

    public static void main(String[] args) throws InterruptedException, MQClientException, RemotingException {
        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-zy-group-async");
        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        producer.start();

        producer.setRetryTimesWhenSendFailed(0);
        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setTopic("rocketmq-zy-topic2");
            message.setTags("rocketmq-zy-t1-tags2");
            message.setBody(String.format("jerry:%s", i).getBytes(StandardCharsets.UTF_8));
            // 异步发送
            int finalI = i;
            producer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult result) {
                    SendStatus sendStatus = result.getSendStatus();
                    String msgId = result.getMsgId();
                    System.out.println(finalI + ".async===============");
                    System.out.println("result:" + result);
                    System.out.println("sendStatus:" + sendStatus);
                    System.out.println("msgId:" + msgId);
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(finalI + ".async exception===============");
                    e.printStackTrace();
                }
            });


            TimeUnit.SECONDS.sleep(1L);
        }

        // 生产环境, 没有这句
        producer.shutdown();
    }
}
