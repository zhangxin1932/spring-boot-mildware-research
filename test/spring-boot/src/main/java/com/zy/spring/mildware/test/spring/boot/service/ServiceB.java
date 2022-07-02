package com.zy.spring.mildware.test.spring.boot.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceB {

    public String hi(String msg) {
        return "b:" + msg;
    }

}
