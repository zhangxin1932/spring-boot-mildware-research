package com.zy.spring.mildware.mq.rocketmq.messagefilter;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TagsConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("consumer-tags-group01");
        consumer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        // 消费该 topic 下所有 tags
        // consumer.subscribe("tags-group01-topic1", "*");
        // 按照 tags 过滤
        consumer.subscribe("tags-group01-topic1", "tags-group01-topic1-tags1 || tags-group01-topic1-tags2");
        consumer.start();

        while (true) {
            List<MessageExt> msgs = consumer.poll();
            msgs.forEach(e -> System.out.println(new String(e.getBody(), StandardCharsets.UTF_8) + "\t" + e));
        }
    }

}
