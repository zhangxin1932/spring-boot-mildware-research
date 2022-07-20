package com.zy.spring.mildware.netty.nio.demo06;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class NioServer02 {

    /**
     * 所有 client 的连接信息
     */
    private static final Map<String, SocketChannel> clientMap = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        // 服务端 channel 并绑定端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8090));
        serverSocketChannel.configureBlocking(false);

        // 将 channel 注册到 selector
        Selector selector = Selector.open();
        // 注册了 serverSocketChannel, 关注连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int select = selector.select();
            // 如果这里有空轮询, 这里会一直打印
            // 据说 jdk1.6 中已解决, 但也有说 jdk1.7 仍有,
            // 本处未发现空轮询, netty 也解决了该问题
            System.out.println("select ---> " + select);

            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                // 清除这个 key
                it.remove();
                // 判断 key 的状态
                if (Objects.nonNull(selectionKey)) {
                    if (selectionKey.isAcceptable()) {
                        System.out.println("----isAcceptable-----");
                        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverChannel.accept();
                        if (Objects.isNull(socketChannel)) {
                            continue;
                        }
                        socketChannel.configureBlocking(false);
                        // 注册了 socketChannel, 关注数据读取事件, 服务端一般不注册 可写事件
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        // 将客户端连接信息写入 clientMap
                        clientMap.put(UUID.randomUUID().toString(), socketChannel);
                    } else if (selectionKey.isReadable()) {
                        System.out.println("------isReadable------");
                        while (true) {
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int len = socketChannel.read(buffer);
                            if (len > 0) {
                                buffer.flip();
                                String msg = String.valueOf(StandardCharsets.UTF_8.decode(buffer).array());
                                AtomicReference<String> atomicSender = new AtomicReference<>();
                                clientMap.forEach((k, v) -> {
                                    if (socketChannel == v) {
                                        atomicSender.set(k);
                                        return;
                                    }
                                });
                                String sender = atomicSender.get();
                                System.out.println(String.format("receive from client %s, msg is: %s", sender, msg));
                                clientMap.forEach((k, v) -> {
                                    if (v != socketChannel) {
                                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                        writeBuffer.put(String.format("%s: send msg: %s.", sender, msg).getBytes());
                                        writeBuffer.flip();
                                        try {
                                            v.write(writeBuffer);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else if (len == -1) {
                                // 解决客户端关闭或者输入结束时的问题
                                System.out.println(String.format("client closed connection---------"));
                                socketChannel.close();
                                break;
                            } else {
                                System.out.println(String.format("len = %s ---------", len));
                                break;
                            }
                        }
                    } else if (selectionKey.isValid()) {
                        System.out.println("------isValid----------");
                    }
                }
            }
        }
    }
}
