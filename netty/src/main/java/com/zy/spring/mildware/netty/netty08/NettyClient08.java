package com.zy.spring.mildware.netty.netty08;

import com.zy.spring.mildware.netty.common.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient08 {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup(4);

        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new FixedLengthFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new ClientHandler08());
                        }
                    });

            ChannelFuture future = bootstrap.connect("127.0.0.1", 8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandler08 extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println(msg);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            byte[] bytes = Constants.paddingBlankBytes("server receive msg: hello", 1024);
            System.out.println(bytes.length);
            ctx.writeAndFlush(Unpooled.copiedBuffer(bytes));
        }
    }
}
