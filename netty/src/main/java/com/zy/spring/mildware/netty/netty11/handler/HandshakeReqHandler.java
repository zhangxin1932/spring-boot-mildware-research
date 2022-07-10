package com.zy.spring.mildware.netty.netty11.handler;

import com.zy.spring.mildware.netty.netty11.constant.Command;
import com.zy.spring.mildware.netty.netty11.constant.Const;
import com.zy.spring.mildware.netty.netty11.pojo.CustomHeader;
import com.zy.spring.mildware.netty.netty11.pojo.CustomMessage;
import com.zy.spring.mildware.netty.netty11.util.ChannelWriteUtil;
import com.zy.spring.mildware.netty.netty11.util.SidUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * <p>
 * 握手请求处理器
 * </p>
 */
@Slf4j
public class HandshakeReqHandler extends ChannelInboundHandlerAdapter {


    /**
     * 在连接通道channel激活时立即发送握手请求
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CustomHeader header = new CustomHeader();
        header.setCommand(Command.HANDSHAKE_REQ.getVal());
        header.setSid(SidUtil.INSTANCE.nextId());
        CustomMessage sendMsg = new CustomMessage();
        sendMsg.setHeader(header);
        ChannelWriteUtil.write(ctx.channel(), sendMsg, future -> {
            if (future.cause() != null) {
                throw new IOException(future.cause());
            } else if (!future.isSuccess()) {
                throw new IOException("client send handshake request fail...");
            } else {
                log.debug("client send handshake request");
            }
        });
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //类型转换
        CustomMessage receiveMsg = (CustomMessage) msg;

        //处理握手应答指令
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.HANDSHAKE_RESP.getVal() == receiveMsg.getHeader().getCommand()) {
            if (Const.SUCCESS == receiveMsg.getHeader().getSuccess()) {
                log.debug("client receive handshake response");
            } else {
                log.error("client receive handshake response fail " + (receiveMsg.getBody() == null ? "" : (String) receiveMsg.getBody()));
                ctx.close();
            }
        }
        //只有成功才继续往下流转
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
