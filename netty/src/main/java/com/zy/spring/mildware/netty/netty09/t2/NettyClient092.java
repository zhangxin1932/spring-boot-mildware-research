package com.zy.spring.mildware.netty.netty09.t2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;

public class NettyClient092 {

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder (102400,0,4,0, 2))
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new StringDecoder())
                                    .addLast(new NettyClientHandler092());
                        }
                    });
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8099).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }


    private static class NettyClientHandler092 extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println("请求报文体 = " + msg);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            byte[] msg = "你好, 中国, 我是客户端".getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(msg);
            ctx.writeAndFlush(buffer);
        }
    }

}
