package com.zy.spring.mildware.mq.disruptor.order;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zy.spring.mildware.mq.disruptor.entity.Order;
import com.zy.spring.mildware.mq.disruptor.factory.OrderFactory;
import com.zy.spring.mildware.mq.disruptor.handler.OrderHandler;
import com.zy.spring.mildware.mq.disruptor.producer.OrderProducer;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 3.单生产者多消费者
 * 单生产者多消费者，多消费者对于消息m独立消费。
 *
 */
public class OneProducerMoreConsumer03 {

    public static void main(String[] args) throws Exception {
        Disruptor<Order> disruptor = null;
        try {
            EventFactory<Order> factory = new OrderFactory();
            int ringBufferSize = 1024 * 1024;
            disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
            /*
             * 两个消费者创建EventHandlerGroup。该消费者需要实现EventHandler类。两个消费者对于RingBuffer中的每个消息，都独立消费一次。
             * 两个消费者在消费消息的过程中，各自独立，不产生竞争。
             */
            disruptor.handleEventsWith(new OrderHandler("1"), new OrderHandler("2"));
            disruptor.start();

            // 单个生产者, 生产数据
            RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
            OrderProducer orderProducer = new OrderProducer(ringBuffer);
            for (int i = 0; i < 5; i++) {
                orderProducer.onData(String.valueOf(i));
            }

            TimeUnit.SECONDS.sleep(60L);
        } finally {
            if (Objects.nonNull(disruptor)) {
                disruptor.shutdown();
            }
        }
    }
}
