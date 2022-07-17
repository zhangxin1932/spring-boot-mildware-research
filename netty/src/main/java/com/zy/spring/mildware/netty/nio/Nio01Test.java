package com.zy.spring.mildware.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Nio01Test {

    public static void main(String[] args) throws IOException {
        // 构造端口号数组
        int[] ports = {8090, 8091, 8092, 8093, 8094};
        // 构造 Selector
        Selector selector = Selector.open();

        // 构造 Channel, 将 Channel 设置为非阻塞的
        for (int i = 0; i < ports.length; i ++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket socket = serverSocketChannel.socket();
            socket.bind(new InetSocketAddress(ports[i]));
            // 将 Channel 注册到 Selector 中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }

        // 循环中等待连接
        while (true) {
            // 这行代码必须写, 否则一直阻塞
            selector.select(60L);
            // 获取选择的 selectionKeys
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                if (Objects.nonNull(selectionKey)) {
                    if (selectionKey.isAcceptable()) {
                        System.out.println("-----isAcceptable-----");
                        ServerSocketChannel selectableChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = selectableChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        // 移除监听
                        it.remove();
                    } else if (selectionKey.isReadable()) {
                        System.out.println("-----isReadable-----");
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        while (true) {
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int len = socketChannel.read(buffer);
                            if (len > 0) {
                                System.out.println("len-----------" + len);
                                buffer.flip();
                                socketChannel.write(buffer);
                                buffer.clear();
                            } else {
                                System.out.println(">>>>>>>>>>>>>>>>>>>>");
                                break;
                            }
                        }
                        // 移除监听
                        it.remove();
                    } else if (selectionKey.isWritable()) {
                        System.out.println("-----isWritable-----");
                    } else if (selectionKey.isConnectable()) {
                        System.out.println("-----isConnectable-----");
                    } else {
                        System.out.println("----------");
                    }
                }
            }
        }
    }
}
