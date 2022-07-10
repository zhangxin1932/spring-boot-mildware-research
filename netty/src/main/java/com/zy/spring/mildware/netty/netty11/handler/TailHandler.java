package com.zy.spring.mildware.netty.netty11.handler;

import com.alibaba.fastjson.JSONObject;
import com.zy.spring.mildware.netty.netty11.constant.Command;
import com.zy.spring.mildware.netty.netty11.pojo.CustomMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TailHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //
        try {
            if (msg != null) {
                CustomMessage message = (CustomMessage) msg;
                if (message.getHeader() != null) {
                    if (Command.of(message.getHeader().getCommand()) == null) {
                        log.error("receive be not illegal command message: " + JSONObject.toJSONString(message));
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
    }
}
