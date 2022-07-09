package com.zy.spring.mildware.mq.disruptor.handler;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.zy.spring.mildware.mq.disruptor.entity.Order;

public class OrderHandler implements EventHandler<Order>, WorkHandler<Order> {

    private final String consumerId;

    public OrderHandler(String consumerId) {
        this.consumerId = consumerId;
    }

    /**
     * 实现 EventHandler 的方法
     * @param order
     * @param sequence
     * @param endOfBatch
     * @throws Exception
     */
    @Override
    public void onEvent(Order order, long sequence, boolean endOfBatch) throws Exception {
        System.out.println("OrderHandler " + this.consumerId + "，消费信息：" + order.getId() + ", sequence: " + sequence);
    }

    /**
     * 实现 WorkHandler 的方法
     * @param order
     * @throws Exception
     */
    @Override
    public void onEvent(Order order) throws Exception {
        System.out.println("OrderHandler " + this.consumerId + "，消费信息：" + order.getId());
    }
}
