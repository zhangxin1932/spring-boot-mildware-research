package com.zy.spring.mildware.misc.eventbus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EventBusTest {

    @Test
    public void fn01() throws Exception {
        CreateOrderDTO createOrderDTO = new CreateOrderDTO();
        createOrderDTO.setOrderId("1");
        AsyncEventBusFactory.post(createOrderDTO);
        TimeUnit.SECONDS.sleep(5L);
        System.out.println("------------------");
        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
        updateOrderDTO.setOrderId("2");
        AsyncEventBusFactory.post(updateOrderDTO);
        TimeUnit.SECONDS.sleep(5L);
    }

}
