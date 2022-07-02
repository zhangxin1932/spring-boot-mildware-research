package com.zy.spring.mildware.test.spring.boot.service;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ServiceA {
    @Resource
    private ServiceB serviceB;

    public String hi(String msg) {
        return "A:" + serviceB.hi(msg);
    }
}
