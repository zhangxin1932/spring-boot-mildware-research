package com.zy.spring.mildware.mq.disruptor.service;

import com.lmax.disruptor.RingBuffer;
import com.zy.spring.mildware.mq.disruptor.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@Service
public class OrderService {
    @Autowired
    private RingBuffer<Order> orderRingBuffer;

    public void publish(String message) {
        log.info("record the message: {}",message);
        //  获取下一个Event槽的下标
        long sequence = orderRingBuffer.next();
        try {
            // 给Event填充数据
            Order event = orderRingBuffer.get(sequence);
            event.setId(message);
            log.info("往消息队列中添加消息：{}", event);
        } catch (Exception e) {
            log.error("failed to add event to OrderRingBuffer for : e = {},{}",e,e.getMessage());
        } finally {
            // 发布Event，激活观察者去消费，将sequence传递给改消费者
            // 注意最后的publish方法必须放在finally中以确保必须得到调用；
            // 如果某个请求的sequence未被提交将会堵塞后续的发布操作或者其他的producer
            orderRingBuffer.publish(sequence);
        }
    }

}
