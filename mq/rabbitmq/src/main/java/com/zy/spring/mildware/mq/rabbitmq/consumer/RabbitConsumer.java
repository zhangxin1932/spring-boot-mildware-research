package com.zy.spring.mildware.mq.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.zy.spring.mildware.mq.rabbitmq.common.RabbitConstants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import java.io.IOException;

public class RabbitConsumer {

    /**
     * 这里模拟 consumer 消费 普通队列
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = { RabbitConstants.DELAY_QUEUE })
    @RabbitHandler
    public void consumeDelayQueue(Message message, Channel channel) throws IOException {
        System.out.println("msg is: " + message.getPayload());
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        // 手工ACK
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 这里模拟 consumer 消费 死信队列
     * @param msg
     */
    @RabbitListener(queues = {RabbitConstants.DLX_QUEUE})
    @RabbitHandler
    public void consumeDlxQueue(String msg) {
        System.out.println("dlx msg ----------" + msg);
    }
}
