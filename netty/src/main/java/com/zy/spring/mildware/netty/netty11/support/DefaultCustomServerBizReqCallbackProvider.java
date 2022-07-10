package com.zy.spring.mildware.netty.netty11.support;

import com.zy.spring.mildware.netty.netty11.custom.CustomServerBizReqCallbackProvider;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DefaultCustomServerBizReqCallbackProvider implements CustomServerBizReqCallbackProvider {


    @Override
    public void execute(Object body, Map<String, Object> headerMap, ChannelHandlerContext ctx) {

        //Do nothing by default
    }


}
