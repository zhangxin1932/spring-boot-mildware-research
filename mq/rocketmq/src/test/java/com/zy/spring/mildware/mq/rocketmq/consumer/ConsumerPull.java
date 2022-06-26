package com.zy.spring.mildware.mq.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ConsumerPull {
    public static void main(String[] args) throws MQClientException {
        // TODO 知识点1: 消费者类型, push(broker向consumer推送消息) or pull类型(消费者主动从broker拉取消息)
        DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("rocketmq-zy-consumer-group-push01");
        consumer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        // TODO 知识点2: 消息过滤模式, tags过滤 or sql语法过滤
        // 消费该 topic 下所有 tags
        // consumer.subscribe("rocketmq-zy-topic1", "*");
        // 按照 tags 过滤
        consumer.subscribe("rocketmq-zy-topic1", "rocketmq-zy-t1-tags1 || rocketmq-zy-t1-tags1");

        // TODO 知识点3: 消费者消费模式, 广播 or 负载均衡
        // 消费者的消费模式之一: 广播模式. 该模式下, 该消费者消费该 topic 下的所有消息
        // consumer.setMessageModel(MessageModel.BROADCASTING);
        // 消费者的消费模式之二: 集群模式, 默认模式. 该模式下, 该消费所在的消费者组, 只有一台机器能消费某一条消息
        // consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.start();

        while (true) {
            List<MessageExt> msgs = consumer.poll();
            msgs.forEach(e -> System.out.println(new String(e.getBody(), StandardCharsets.UTF_8) + "\t" + e));
        }
    }
}
