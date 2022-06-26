package com.zy.spring.mildware.mq.rocketmq.messageorder;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * 顺序消息消费, 有问题, 待处理!!!!!!
 */
public class OrderedConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("orderedConsumerGroup");
        consumer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        consumer.subscribe(OrderedProducer.TOPIC, OrderedProducer.TAGS);

        // MessageListenerOrderly: 会使得一个线程仅仅监听一个 MessageQueue 里的消息.
        // 配合对应的消息生产者, 共同实现顺序消息
        consumer.registerMessageListener((MessageListenerOrderly) (msgs, context) -> {
            // TODO 观察, 同一个 orderId 是否被同一个线程消费
            // FIXME 这里有2个问题
            // 1.只启动一个consumer节点: 首次启动完 producer 和 consumer 后, 没问题. 当再次启动 producer 后, 发现 consumer 对于同一个 queueId 不是用同一个线程消费了!!!
            // 2.当启动多个, 如4个consumer节点时, 虽然不同queue的消息发到了同一个节点上, 但是同一个节点上却也是多个线程消费, 同一个节点上也是无法保证顺序的.
            msgs.forEach(msg -> System.out.println(Thread.currentThread().getName() + " --> queueId is --> " + context.getMessageQueue().getQueueId() + "; msg is --> " + JSON.parse(msg.getBody())));
            return ConsumeOrderlyStatus.SUCCESS;
        });

        consumer.start();
    }
}
