package com.zy.spring.mildware.netty.netty11.handler;

import com.zy.spring.mildware.netty.netty11.constant.Command;
import com.zy.spring.mildware.netty.netty11.constant.Const;
import com.zy.spring.mildware.netty.netty11.pojo.CustomHeader;
import com.zy.spring.mildware.netty.netty11.pojo.CustomMessage;
import com.zy.spring.mildware.netty.netty11.util.ChannelWriteUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 心跳应答处理器
 * </p>
 */
@Slf4j
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        CustomMessage receiveMsg = (CustomMessage) msg;
        //参数检查
        if (receiveMsg != null && receiveMsg.getHeader() != null) {
            //接收到心跳请求，
            if (Command.HEARTBEAT_REQ.getVal() == receiveMsg.getHeader().getCommand()) {
                log.debug("server receive heartbeat request");
                //应答心跳请求
                CustomMessage sendMsg = new CustomMessage();
                CustomHeader header = new CustomHeader();
                header.setCommand(Command.HEARTBEAT_RESP.getVal());
                header.setSuccess(Const.SUCCESS);
                sendMsg.setHeader(header);
                ChannelWriteUtil.write(ctx.channel(), sendMsg, future -> {
                    if (future.isSuccess()) {
                        log.debug("server send heartbeat response");
                    } else {
                        log.error("server send heartbeat response fail..", future.cause());
                    }
                });
            }
        }

        ctx.fireChannelRead(msg);
    }

}
