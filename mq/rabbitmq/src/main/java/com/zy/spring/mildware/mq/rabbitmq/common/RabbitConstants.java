package com.zy.spring.mildware.mq.rabbitmq.common;

public interface RabbitConstants {

    /**
     * 延迟队列 queue 名称
     */
    String DELAY_QUEUE = "delayQueue";
    /**
     * 延迟队列 exchange 名称
     */
    String DELAY_EXCHANGE = "delayExchange";
    /**
     * 延迟队列 exchange 对应的 routing key
     */
    String DELAY_ROUTING_KEY = "delayRoutingKey";

    /**
     * 死信队列 queue 名称
     */
    String DLX_QUEUE = "dlxQueue";
    /**
     * 死信队列 exchange 名称
     */
    String DLX_EXCHANGE = "dlxExchange";
    /**
     * 死信队列 exchange 对应的 routing key
     */
    String DLX_ROUTING_KEY = "dlxRoutingKey";
    /**
     * 死信队列 参数 exchange
     * 由 rabbitmq 决定
     */
    String DLX_EXCHANGE_KEY = "x-dead-letter-exchange";
    /**
     * 死信队列 参数 routing key
     * 由 rabbitmq 决定
     */
    String DLX_ROUTING_KEY_KEY = "x-dead-letter-routing-key";
}
