package com.zy.spring.mildware.netty.netty11.handler;

import com.zy.spring.mildware.netty.netty11.constant.Command;
import com.zy.spring.mildware.netty.netty11.constant.Const;
import com.zy.spring.mildware.netty.netty11.custom.CustomSecurityAuthProvider;
import com.zy.spring.mildware.netty.netty11.pojo.CustomHeader;
import com.zy.spring.mildware.netty.netty11.pojo.CustomMessage;
import com.zy.spring.mildware.netty.netty11.pojo.CustomSecurity;
import com.zy.spring.mildware.netty.netty11.util.ChannelWriteUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ServiceLoader;

/**
 * <p>
 * 握手应答处理器
 * </p>
 */
@Slf4j
public class HandshakeRespHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //类型转换
        CustomMessage receiveMsg = (CustomMessage) msg;

        //参数检查
        if (receiveMsg != null && receiveMsg.getHeader() != null && Command.HANDSHAKE_REQ.getVal() == receiveMsg.getHeader().getCommand()) {
            //检查协议魔数
            if (Const.MAJOR != receiveMsg.getHeader().getMajor()) {
                ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol major check fail，" + receiveMsg.getHeader().getMajor()));
                return;
            }

            //检查主版本
            else if (Const.MAIN_VERSION != receiveMsg.getHeader().getMainVersion()) {
                ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol main version check fail，" + receiveMsg.getHeader().getMainVersion()));
            }

            //检查次版本
            else if (Const.MINOR_VERSION != receiveMsg.getHeader().getMinorVersion()) {
                ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol minor version check fail，" + receiveMsg.getHeader().getMinorVersion()));
            }

            //安全认证 || 握手应答
            else {

                CustomSecurity security = new CustomSecurity();
                security.setHeader(receiveMsg.getHeader());
                security.setRemoteAddress((InetSocketAddress) ctx.channel().remoteAddress());
                //基于JDK自带SPI技术实现安全认证
                ServiceLoader<CustomSecurityAuthProvider> loader = ServiceLoader.load(CustomSecurityAuthProvider.class);
                boolean isOk = true;
                for (CustomSecurityAuthProvider service : loader) {
                    String errorMsg = service.isAllow(security);
                    if (errorMsg != null) {
                        isOk = false;
                        ChannelWriteUtil.write(ctx.channel(), buildFailMessage("xiaot protocol security check fail, " + errorMsg));
                        break;
                    }
                }

                if (isOk) {
                    log.debug("server receive handshake request...");
                    //应答握手
                    CustomMessage respMsg = new CustomMessage();
                    CustomHeader respHeader = new CustomHeader();
                    respHeader.setSuccess(Const.SUCCESS);
                    respHeader.setCommand(Command.HANDSHAKE_RESP.getVal());
                    respMsg.setHeader(respHeader);
                    ChannelWriteUtil.write(ctx.channel(), respMsg, new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
                                log.debug("server send handshake response");
                            } else {
                                log.error("server send handshake response fail...", future.cause());
                            }
                        }
                    });
                }
            }
        }

        ctx.fireChannelRead(msg);
    }

    private CustomMessage buildFailMessage(String body) {
        CustomMessage respMsg = new CustomMessage();
        CustomHeader respHeader = new CustomHeader();
        respHeader.setCommand(Command.HANDSHAKE_RESP.getVal());
        respHeader.setSuccess(Const.FAIL);
        respMsg.setHeader(respHeader);
        respMsg.setBody(body);
        log.error(body);
        return respMsg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
