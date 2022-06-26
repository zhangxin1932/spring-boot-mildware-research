package com.zy.spring.mildware.mq.rocketmq.producer;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 可以参考书籍 <<RocketMQ实战与原理解析>>
 * 这里的 demo 暂时放到了 本工程的 test 包下
 * <p>
 * 消息发送者步骤分析
 * 1.创建消息生产者 Producer,并指定生产者组名
 * 2.指定 nameserver 地址
 * 3.启动 Producer
 * 4•创建消息对象，指定 Topic、Tag和消息体
 * 5.发送消息
 * 6.关闭生产者 Producer
 */
@Component
public class RocketmqProducer {

    @Value("${rocketmq.custom.topic}")
    private String topic;

    @Value("${rocketmq.custom.tags}")
    private String tags;

    @Resource
    private RocketMQTemplate mqTemplate;

    public void send(final Object object) {
        Message<Object> message = MessageBuilder.withPayload(object).build();
        mqTemplate.send(String.format("%s:%s", topic, tags), message);
    }

}
