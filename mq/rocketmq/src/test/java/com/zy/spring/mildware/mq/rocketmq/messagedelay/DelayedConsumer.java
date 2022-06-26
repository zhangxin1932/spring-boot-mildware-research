package com.zy.spring.mildware.mq.rocketmq.messagedelay;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DelayedConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("delayedConsumerGroup");
        consumer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        consumer.subscribe(DelayedProducer.TOPIC, DelayedProducer.TAGS);
        consumer.start();

        while (true) {
            List<MessageExt> msgs = consumer.poll();
            msgs.forEach(e -> System.out.println(new String(e.getBody(), StandardCharsets.UTF_8) + "\t" + (System.currentTimeMillis() - e.getStoreTimestamp())));
        }
    }
}
