package com.zy.spring.mildware.mq.rocketmq.messagefilter;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class TagsProducer {
    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer();

        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");
        producer.setProducerGroup("producer-tags-group01");

        producer.start();

        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setTopic("tags-group01-topic1");
            message.setTags("tags-group01-topic1-tags1");
            message.setBody(String.format("tom:%s", i).getBytes(StandardCharsets.UTF_8));
            // 同步发送
            SendResult result = producer.send(message);
            System.out.println(i + ".===============");
            System.out.println("result:" + result);

            TimeUnit.SECONDS.sleep(1L);
        }

        // 生产环境, 没有这句
        producer.shutdown();
    }
}
