package com.zy.spring.mildware.netty.netty11.pojo;

import lombok.Data;

import java.net.InetSocketAddress;

@Data
public class CustomSecurity {

    private CustomHeader header;
    private InetSocketAddress remoteAddress;

}
