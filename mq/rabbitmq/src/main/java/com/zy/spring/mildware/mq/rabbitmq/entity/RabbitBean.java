package com.zy.spring.mildware.mq.rabbitmq.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RabbitBean implements Serializable {
    private String name;
    private Integer age;
}
