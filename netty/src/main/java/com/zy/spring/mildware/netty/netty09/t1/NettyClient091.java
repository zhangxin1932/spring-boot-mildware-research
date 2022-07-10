package com.zy.spring.mildware.netty.netty09.t1;

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

import java.nio.charset.StandardCharsets;

public class NettyClient091 {
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
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2))
                                    .addLast(new NettyClientHandler091());
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


    private static class NettyClientHandler091 extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
            int length = msg.readableBytes();
            System.out.println("请求总长度 = " + length);
            // 首先读取两个字节的长度位
            short len = msg.readShort();
            System.out.println("请求报文体的长度 = " + len);
            System.out.println("读取两个字节后的ByteBuf = " + msg);
            // 读取报文内容
            byte[] bytes = new byte[len];
            msg.readBytes(bytes, 0, len);
            String request = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("请求报文体 = " + request);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            byte[] msg = "你好, 中国, 我是客户端".getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeShort(msg.length);
            buffer.writeBytes(msg);
            ctx.writeAndFlush(buffer);
        }
    }

}
