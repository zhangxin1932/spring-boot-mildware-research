package com.zy.spring.mildware.mq.disruptor.factory;

import com.lmax.disruptor.EventFactory;
import com.zy.spring.mildware.mq.disruptor.entity.Order;

public class OrderFactory implements EventFactory<Order> {
    @Override
    public Order newInstance() {
        return new Order();
    }
}
