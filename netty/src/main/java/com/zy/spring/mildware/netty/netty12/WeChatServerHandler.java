package com.zy.spring.mildware.netty.netty12;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Objects;

public class WeChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 保存所有与 服务端 建立好连接的 客户端的 channel 对象
     */
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 服务端 收到 任意一个 客户端传递过来的消息, 该方法即被调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取向服务端发送消息的 客户端 连接
        Channel channel = ctx.channel();
        // 遍历channelGroup
        channelGroup.forEach(ch -> {
            // 如果 channel 是 发送消息的 客户端连接
            if (Objects.equals(ch, channel)) {
                // 要加上:---------------> \n
                ch.writeAndFlush("self send msg: " + msg + "\n");
                // 如果 channel 不是 发送消息的 客户端连接
            } else {
                // 要加上:---------------> \n
                ch.writeAndFlush("remoteAddress: " + channel.remoteAddress() + " -> send msg: " + msg + "\n");
            }
        });
    }

    /**
     * 有客户端连接加入
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 广播
        channelGroup.writeAndFlush("remoteAddress: " + channel.remoteAddress() + " -> join the server.");
        channelGroup.add(channel);
    }

    /**
     * 有客户端连接断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 广播
        channelGroup.writeAndFlush("remoteAddress: " + channel.remoteAddress() + " -> leave the server.");
        // 这行代码, 可以省略, netty 会自动清理断开的连接
        channelGroup.remove(channel);
    }

    /**
     * 客户端处于活动状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("remoteAddress: " + channel.remoteAddress() + " is online.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("remoteAddress: " + channel.remoteAddress() + " is offline.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
