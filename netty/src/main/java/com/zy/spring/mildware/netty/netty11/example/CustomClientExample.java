package com.zy.spring.mildware.netty.netty11.example;

import com.zy.spring.mildware.netty.netty11.bootstrap.CustomNettyClient;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * https://blog.csdn.net/lzy_zhi_yuan/article/details/117915333
 * https://github.com/lazy-share/xiaot-protocol
 * </p>
 */
@Slf4j
public class CustomClientExample {

    public static void main(String[] args) throws InterruptedException {

        //实例化小T协议客户端
        CustomNettyClient client = new CustomNettyClient("localhost", 9000);

        //客户端启动三条线程同时通过小T协议客户端向服务端发送数据
        new Thread(new Work(client, 20)).start();
        new Thread(new Work(client, 30)).start();
        new Thread(new Work(client, 50)).start();

        //阻塞主线程
        TimeUnit.HOURS.sleep(10000);
    }

    public static class Work implements Runnable {

        CustomNettyClient client;
        long sleep;
        public Work(CustomNettyClient client, long sleep){
            this.client = client;
            this.sleep = sleep;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleep);
                    client.sendMessage(sleep + " zhangsan sayHello .................................", null, new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if (future.isSuccess()) {
//                                    log.info("send success...");
                            } else {
                                log.error("send fail...", future.cause());
                            }
                        }
                    });
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
