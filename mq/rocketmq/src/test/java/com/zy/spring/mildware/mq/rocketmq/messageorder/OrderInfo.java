package com.zy.spring.mildware.mq.rocketmq.messageorder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderInfo {
    private Long orderId;
    private String status;
}
