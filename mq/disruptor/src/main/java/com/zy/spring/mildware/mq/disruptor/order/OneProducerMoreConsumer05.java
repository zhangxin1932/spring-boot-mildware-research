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
 * 5.单生产者多消费者
 * 多消费者之间不重复消费，且不同的消费者WorkPool之间存在依赖关系。
 *
 */
public class OneProducerMoreConsumer05 {

    public static void main(String[] args) throws Exception {
        Disruptor<Order> disruptor = null;
        try {
            EventFactory<Order> factory = new OrderFactory();
            int ringBufferSize = 1024 * 1024;
            disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());
            /*
             * 单生产者，多消费者。多消费者之间不重复消费，且不同的消费者WorkPool之间存在依赖关系。
             * 消费者1、2不重复消费消息，消费者3、4不重复消费1或者2消费过的消息，消费者5消费消费者3或4消费过的消息。
             */
            disruptor.handleEventsWithWorkerPool(new OrderHandler("1"), new OrderHandler("2"))
                    .thenHandleEventsWithWorkerPool(new OrderHandler("3"), new OrderHandler("4"))
                    .thenHandleEventsWithWorkerPool(new OrderHandler("5"));
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
