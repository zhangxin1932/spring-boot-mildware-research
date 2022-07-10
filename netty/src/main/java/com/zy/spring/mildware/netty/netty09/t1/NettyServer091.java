package com.zy.spring.mildware.netty.netty09.t1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.charset.StandardCharsets;

/**
 * LengthFieldBasedFrameDecoder
 *
 * 该解码器根据消息中的 length 字段的值动态拆分接收到的 ByteBuf，
 * 在解码二进制报文（包含报文头：报文头存储报文的长度）时，此解码器特别有用
 *
 * https://wenku.baidu.com/view/1025353ba000a6c30c22590102020740be1ecd25.html
 * https://www.jianshu.com/p/6a55b9fe4f10
 */
public class NettyServer091 {

    public static void main(String[] args) {
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2))
                                    .addLast(new NettyServerHandler091());
                        }
                    });
            ChannelFuture future = server.bind("127.0.0.1", 8099).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static class NettyServerHandler091 extends SimpleChannelInboundHandler<ByteBuf> {

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

            byte[] response = "你好, 中国, 我是服务端".getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeShort(response.length);
            buffer.writeBytes(response);
            channelHandlerContext.writeAndFlush(buffer);
        }
    }

}
