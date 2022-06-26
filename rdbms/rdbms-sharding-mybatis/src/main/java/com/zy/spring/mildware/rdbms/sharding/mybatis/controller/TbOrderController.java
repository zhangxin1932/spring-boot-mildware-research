package com.zy.spring.mildware.rdbms.sharding.mybatis.controller;

import com.zy.spring.mildware.rdbms.sharding.mybatis.mapper.TbOrderMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order/")
public class TbOrderController {

    @Resource
    private TbOrderMapper tbOrderMapper;

    @RequestMapping("insert")
    public void insert(String orderName) {
        tbOrderMapper.insert(orderName);
    }

    @RequestMapping("selectOrderById")
    public void selectOrderById(Long id) {
        tbOrderMapper.selectOrderById(id);
    }

    @RequestMapping("selectOrders")
    public void selectOrders() {
        tbOrderMapper.selectOrders();
    }

}
