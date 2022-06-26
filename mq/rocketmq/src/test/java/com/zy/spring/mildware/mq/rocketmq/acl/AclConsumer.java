package com.zy.spring.mildware.mq.rocketmq.acl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

public class AclConsumer {
    public static void main(String[] args) throws Exception{
        AclClientRPCHook rpcHook = new AclClientRPCHook(new SessionCredentials(AclProducer.ACCESS_KEY, AclProducer.SECRET_KEY));
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(rpcHook);
        consumer.setConsumerGroup("rocketmq-zy-group-consumer");
        consumer.setNamesrvAddr("192.168.0.156:9876");
        consumer.subscribe(AclProducer.TOPIC, AclProducer.TAGS);
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            if(CollectionUtils.isNotEmpty(msgs)) {
                msgs.forEach(System.out::println);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
    }
}
