package com.zy.spring.mildware.mq.disruptor.producer;

import com.lmax.disruptor.RingBuffer;
import com.zy.spring.mildware.mq.disruptor.entity.Order;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderProducer {

    private final RingBuffer<Order> ringBuffer;

    public void onData(String data) {
        long sequence = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequence);
            order.setId(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

}
