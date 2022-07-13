package com.zy.spring.mildware.netty.netty12;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

/**
 * 聊天室
 */
public class WeChatServer01 {

    public static void main(String[] args) {
        // EventLoopGroup 就是一个Reactor
        // bossGroup or parentGroup 接收客户端连接, 转发给 workerGroup or childGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // workerGroup or childGroup 对请求进行真正的处理, 如果这里构造器, 不传值, 默认线程数是:
        // Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2))
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childAttr(AttributeKey.newInstance("childAttr"), "childAttrValue")
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            // channelPipeline 里面都是 handler, 当请求到来时, 将从管道中的首个 handler 到最后一个; 响应时, 也是如此
                            ChannelPipeline pipeline = ch.pipeline();
                            // 像管道一样, 将一个一个的处理器 添加到管道的尾部: addLast, 因此处理器的顺序可能影响数据解析
                            // netty 会自动将 入站 与 出站处理器区分开来
                            pipeline.addLast(new DelimiterBasedFrameDecoder(4096, Delimiters.lineDelimiter()));
                            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast(new WeChatServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
