package com.zy.spring.mildware.mq.rocketmq.messagefilter;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class SqlConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("consumer-sql-group01");
        consumer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        // 按照 sql 过滤, 此处 up 来自 Producer 方定义的属性
        consumer.subscribe("sql-group01-topic1", MessageSelector.bySql("up > 2"));
        consumer.start();

        while (true) {
            List<MessageExt> msgs = consumer.poll();
            msgs.forEach(e -> System.out.println(new String(e.getBody(), StandardCharsets.UTF_8) + "\t" + e));
        }
    }

}
