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
 * 4.单生产者多消费者
 * 单生产者，多消费者间存在依赖关系的模式。
 * 消费者1、2消息独立消费。
 * 消费者3、4仅能消费1、2均消费过的消息，消费者5仅能消费3、4均消费过的消息
 *
 */
public class OneProducerMoreConsumer04 {

    public static void main(String[] args) throws Exception {
        Disruptor<Order> disruptor = null;
        try {
            EventFactory<Order> factory = new OrderFactory();
            int ringBufferSize = 1024 * 1024;
            disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
            //相当于在各个EventHandlerGroup之间进行级联，形成依赖关系。
            disruptor.handleEventsWith(new OrderHandler("1"), new OrderHandler("2"))
                    .then(new OrderHandler("3"), new OrderHandler("4"))
                    .then(new OrderHandler("5"));
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
