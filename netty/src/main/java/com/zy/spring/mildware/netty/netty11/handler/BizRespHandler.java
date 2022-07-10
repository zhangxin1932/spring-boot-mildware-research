package com.zy.spring.mildware.netty.netty11.handler;

import com.alibaba.fastjson.JSONObject;
import com.zy.spring.mildware.netty.netty11.constant.Command;
import com.zy.spring.mildware.netty.netty11.custom.CustomClientBizRespCallbackProvider;
import com.zy.spring.mildware.netty.netty11.pojo.CustomMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ServiceLoader;

/**
 * <p>
 * 业务应答处理器
 * </p>
 */
@Slf4j
public class BizRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //类型转换
        CustomMessage receiveMsg = (CustomMessage) msg;

        //业务应答指令
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.BIZ_RESP.getVal() == receiveMsg.getHeader().getCommand()) {
            log.debug("client receive biz respon : {}", JSONObject.toJSONString(receiveMsg));
            //基于JDK自带的SPI技术实现协议业务处理的自定义扩展开发
            //默认将接收到的业务请求回调XiaotBizRespCallbackProvide接口execute方法
            ServiceLoader<CustomClientBizRespCallbackProvider> loader = ServiceLoader.load(CustomClientBizRespCallbackProvider.class);
            for (CustomClientBizRespCallbackProvider service : loader) {
                service.execute(receiveMsg.getBody(), receiveMsg.getHeader().getAttribute(), ctx);
            }
        }
        ctx.fireChannelRead(msg);
    }
}
