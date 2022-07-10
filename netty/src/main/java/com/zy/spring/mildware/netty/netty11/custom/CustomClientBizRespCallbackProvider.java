package com.zy.spring.mildware.netty.netty11.custom;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

public interface CustomClientBizRespCallbackProvider {
    void execute(Object body, Map<String, Object> headerMap, ChannelHandlerContext ctx);
}
