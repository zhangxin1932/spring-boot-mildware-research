package com.zy.spring.mildware.netty.netty11.util;

import com.zy.spring.mildware.netty.netty11.pojo.CustomMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class ChannelWriteUtil {


    public static ChannelFuture write(Channel channel,
                                      CustomMessage message) {

        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        if (!channel.isWritable()) {
            throw new ChannelException("channel not writable");
        }

        ChannelFuture future = channel.writeAndFlush(message);
        if (!future.isSuccess()) {
            throw new ChannelException("channel write fail", future.cause());
        }
        return future;
    }

    public static ChannelFuture write(Channel channel,
                                      CustomMessage message,
                                      GenericFutureListener<? extends Future<? super Void>> futureListener) {

        if (channel == null) {
            throw new NullPointerException("channel is null");
        }
        if (!channel.isWritable()) {
            throw new ChannelException("channel not writable");
        }

        ChannelFuture future = channel.writeAndFlush(message);
        if (future.cause() != null) {
            throw new ChannelException("channel write fail", future.cause());
        }
        if (futureListener != null) {
            future.addListener(futureListener);
        }
        return future;
    }
}
