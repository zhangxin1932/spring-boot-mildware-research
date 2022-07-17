package com.zy.spring.mildware.netty.netty13;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * https://mp.weixin.qq.com/s?__biz=MzU0OTk3ODQ3Ng==&mid=2247487728&idx=1&sn=9b99e668af47f5e420f585e5ef8d3632&chksm=fba6f8f3ccd171e571a2eb5eec7f68ebfb8b1b32b869aca4fc11ec25cc3114500cadaa4a5d54&mpshare=1&scene=24&srcid=&sharer_sharetime=1590315226080&sharer_shareid=578e46a83c2f380ecc2d332d96538cf4#rd
 */
public class Server {

    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加用于处理粘包和拆包问题的处理器
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                            pipeline.addLast(new LengthFieldPrepender(4));
                            // 添加自定义协议消息的编码和解码处理器
                            pipeline.addLast(new MessageEncoder());
                            pipeline.addLast(new MessageDecoder());
                            // 添加具体的消息处理器
                            pipeline.addLast(new ServerMessageHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(8585).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
