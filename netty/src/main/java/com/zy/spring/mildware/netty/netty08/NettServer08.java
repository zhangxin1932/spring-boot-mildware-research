package com.zy.spring.mildware.netty.netty08;

import com.zy.spring.mildware.netty.common.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * netty 粘包拆包 handler: 消息定长处理器
 */
public class NettServer08 {

    public static void main(String[] args) {
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());

        try {
            server.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    // 1024 是字节
                                    .addLast(new FixedLengthFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new ServerHandler08());
                        }
                    });

            ChannelFuture future = server.bind("127.0.0.1", 8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    private static class ServerHandler08 extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("req: " + msg);
            byte[] bytes = Constants.paddingBlankBytes("server receive msg: hello", 1024);
            ctx.writeAndFlush(Unpooled.copiedBuffer(bytes));
        }

    }

}
