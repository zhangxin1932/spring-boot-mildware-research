package com.zy.spring.mildware.mq.rocketmq.messagedelay;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

public class DelayedProducer {

    public static final String TOPIC = "delayedTopic";
    public static final String TAGS = "delayedTags";

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("delayedProducerGroup");
        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        producer.start();

        for (int i = 0; i < 5; i ++) {
            Message msg = new Message();
            msg.setTopic(TOPIC);
            msg.setTags(TAGS);
            msg.setBody(("hello: " + i).getBytes(StandardCharsets.UTF_8));
            // 设置延迟时间的级别
            msg.setDelayTimeLevel(2);
            SendResult result = producer.send(msg);
            System.out.println(result);
        }

        producer.shutdown();
    }
}
