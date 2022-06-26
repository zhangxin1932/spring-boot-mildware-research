package com.zy.spring.mildware.mq.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

public class ConsumerPush {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("rocketmq-zy-consumer-group-push01");
        consumer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        consumer.subscribe("rocketmq-zy-topic1", "rocketmq-zy-t1-tags1");
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            msgs.forEach(e -> System.out.println(Thread.currentThread().getName() + " ---> " + e));
            return ConsumeOrderlyStatus.SUCCESS;
        });

        consumer.start();
    }
}
