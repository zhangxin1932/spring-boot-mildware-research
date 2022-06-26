package com.zy.spring.mildware.mq.rabbitmq.config;

import com.zy.spring.mildware.mq.rabbitmq.common.RabbitConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitmqConfiguration {


    @Resource
    private Queue dlxQueue;

    @Resource
    private Exchange dlxExchange;

    ///////////////////////////////// 普通队列 与 死信队列 配置开始 /////////////////////////////////
    // step1: 这里定义了一个延时队列(需在 producer 端设置延时时间), 以及发生故障时, 消息转发到的 "死信队列" 的配置
    @Bean
    public Exchange delayExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DELAY_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue delayQueue() {
        Map<String, Object> params = new HashMap<>();
        // 延迟队列里的 死信 转发到的 exchange 名称
        params.put(RabbitConstants.DLX_EXCHANGE_KEY, RabbitConstants.DLX_EXCHANGE);
        // 延迟队列里的 死信 转发到的 routing key 名称
        params.put(RabbitConstants.DLX_ROUTING_KEY_KEY, RabbitConstants.DLX_ROUTING_KEY);
        return QueueBuilder.durable(RabbitConstants.DELAY_QUEUE).withArguments(params).build();
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(RabbitConstants.DELAY_ROUTING_KEY).noargs();
    }

    // step2: 这里是 "step1" 故障时, 对应的 "死信队列" 的配置, 以便于 "死信" 被监听此 "死信队列"  的 consumer 消费
    @Bean
    public Exchange dlxExchange() {
        return ExchangeBuilder.directExchange(RabbitConstants.DLX_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue dlxQueue() {
        return QueueBuilder.durable(RabbitConstants.DLX_QUEUE).build();
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(RabbitConstants.DLX_ROUTING_KEY).noargs();
    }
    ///////////////////////////////// 普通队列 与 死信队列 配置结束 /////////////////////////////////
}