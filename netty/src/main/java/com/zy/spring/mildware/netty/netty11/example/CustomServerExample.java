package com.zy.spring.mildware.netty.netty11.example;

import com.zy.spring.mildware.netty.netty11.bootstrap.CustomNettyServer;

public class CustomServerExample {

    public static void main(String[] args) throws Exception {
        //通过本地main方法启动服务端
        new CustomNettyServer().bind(9000);
    }
}
