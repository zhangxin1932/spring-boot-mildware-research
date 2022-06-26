package com.zy.spring.mildware.mq.rocketmq.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 消息消费者骤分析
 * 1.创建消费者 Consumer, 指定消费者组名
 * 2.指定 nameserver 地址
 * 3.订阅 Topic 和 Tag
 * 4.设置回调函数，处理肖息
 * 5.启动消费者 Consumer
 */
@RocketMQMessageListener(topic = "${rocketmq.custom.topic}",
        selectorExpression ="${rocketmq.custom.tags}",
        consumerGroup = "${rocketmq.custom.consumer-group}")
@Component
public class RocketmqConsumer implements RocketMQListener<Object> {
    @Override
    public void onMessage(Object message) {
        System.out.println("消费者收到消息: " + message);
    }
}
