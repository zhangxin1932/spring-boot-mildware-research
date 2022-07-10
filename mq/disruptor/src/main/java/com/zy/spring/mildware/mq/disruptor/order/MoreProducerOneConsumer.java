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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 8.多生产者，单消费者模式
 * 该场景较为简单，
 * 只需将ProducerType.SINGLE改为ProducerType.MULTI，
 * 并且编写多线程生产者的相关代码即可。
 *
 */
public class MoreProducerOneConsumer {

    public static void main(String[] args) throws Exception {
        Disruptor<Order> disruptor = null;
        try {
            EventFactory<Order> factory = new OrderFactory();
            int ringBufferSize = 1024 * 1024;
            //ProducerType要设置为MULTI，后面才可以使用多生产者模式
            disruptor = new Disruptor<>(factory, ringBufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());
            //简化问题，设置为单消费者模式，也可以设置为多消费者及消费者间多重依赖。
            disruptor.handleEventsWith(new OrderHandler("1"));
            disruptor.start();

            final RingBuffer<Order> ringBuffer = disruptor.getRingBuffer();
            //判断生产者是否已经生产完毕
            final CountDownLatch countDownLatch = new CountDownLatch(3);
            //单生产者，生产3条数据
            for (int l = 0; l < 3; l++) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        for(int i = 0; i < 3; i++) {
                            new OrderProducer(ringBuffer).onData(Thread.currentThread().getName() + "'s " + i + "th message");
                        }
                        countDownLatch.countDown();
                    }
                };
                thread.setName("producer thread " + l);
                thread.start();
            }
            countDownLatch.await();

            TimeUnit.SECONDS.sleep(60L);
        } finally {
            if (Objects.nonNull(disruptor)) {
                disruptor.shutdown();
            }
        }
    }
}
