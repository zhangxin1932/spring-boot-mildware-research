package com.zy.spring.mildware.rdbms.sharding.mybatis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbOrder implements Serializable {
    private Long id;
    private Long orderId;
    private String orderName;
}
