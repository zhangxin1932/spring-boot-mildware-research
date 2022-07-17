package com.zy.spring.mildware.mq.disruptor.config;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zy.spring.mildware.mq.disruptor.entity.Order;
import com.zy.spring.mildware.mq.disruptor.factory.OrderFactory;
import com.zy.spring.mildware.mq.disruptor.handler.OrderHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
public class MQManager {
    public static final ThreadFactory THREAD_FACTORY = Executors.defaultThreadFactory();

    @Bean("order")
    public RingBuffer<Order> orderRingBuffer() {

        // 指定事件工厂
        OrderFactory factory = new OrderFactory();

        // 指定 RingBuffer 字节大小，必须为2的N次方（能将求模运算转为位运算提高效率），否则将影响效率
        int bufferSize = 1024 * 256;

        // 单线程模式，获取额外的性能
        Disruptor<Order> disruptor = new Disruptor<>(factory, bufferSize, THREAD_FACTORY,
                ProducerType.SINGLE, new BlockingWaitStrategy());

        // 设置事件业务处理器---消费者
        disruptor.handleEventsWith(new OrderHandler("default"));

        // 启动 disruptor 线程
        disruptor.start();

        //获取 RingBuffer 环，用于接取生产者生产的事件
        return disruptor.getRingBuffer();
    }

}