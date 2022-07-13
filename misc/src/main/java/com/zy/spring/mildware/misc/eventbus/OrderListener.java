package com.zy.spring.mildware.misc.eventbus;

import com.google.common.eventbus.Subscribe;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderListener {

    @Subscribe
    public void createOrder(CreateOrderDTO createOrderDTO) {
        System.out.println("begin to create order:" + createOrderDTO.getOrderId());
    }

    @Subscribe
    public void updateOrder(UpdateOrderDTO updateOrderDTO) {
        System.out.println("begin to update order:" + updateOrderDTO.getOrderId());
    }

    @PostConstruct
    public void init() {
        AsyncEventBusFactory.register(this);
    }
}
