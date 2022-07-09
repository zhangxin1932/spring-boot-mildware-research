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
 * 2.单生产者多消费者
 * 多消费者间形成依赖关系，每个依赖节点只有一个消费者。
 * 多消费者对于消息重复消费
 *
 * C1 --> C2/C3 --> C4
 * 消费者C2、C3只有在C1消费完消息m后，才能消费m。
 * 消费者C4只有在C2、C3消费完m后，才能消费该消息。
 */
public class OneProducerMoreConsumer01 {

    public static void main(String[] args) throws Exception {
        Disruptor<Order> disruptor = null;
        try {
            EventFactory<Order> factory = new OrderFactory();
            int ringBufferSize = 1024 * 1024;
            disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());

            // 多个消费者间形成依赖关系，每个依赖节点的消费者为单线程。
            disruptor.handleEventsWith(new OrderHandler("C1"))
                    .then(new OrderHandler("C2"), new OrderHandler("C3"))
                    .then(new OrderHandler("C4"));
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
