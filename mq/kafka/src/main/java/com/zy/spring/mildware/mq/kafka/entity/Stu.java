package com.zy.spring.mildware.mq.kafka.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Stu {
    private Integer id;
    private String name;
    private Integer age;
}
