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
 * 调用handleEventsWithWorkerPool形成WorkerPool，并进一步封装成EventHandlerGroup。
 * 对于同一条消息，两消费者不重复消费。
 */
public class OneProducerMoreConsumer02 {

    public static void main(String[] args) throws Exception {
        Disruptor<Order> disruptor = null;
        try {
            EventFactory<Order> factory = new OrderFactory();
            int ringBufferSize = 1024 * 1024;
            disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new YieldingWaitStrategy());

            /*
             * 该方法传入的消费者需要实现WorkHandler接口，方法的内部实现是：先创建WorkPool，然后封装WorkPool为EventHandlerPool返回。
             * 消费者1、2对于消息的消费有时有竞争，保证同一消息只能有一个消费者消费
             */
            disruptor.handleEventsWithWorkerPool(new OrderHandler("1"),
                    new OrderHandler("2"));
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
