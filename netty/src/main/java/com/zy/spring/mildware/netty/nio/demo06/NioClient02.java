package com.zy.spring.mildware.netty.nio.demo06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioClient02 {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        // 这里要写在 注册 后面
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8090));

        while (true) {
            int select = selector.select();
            // System.out.println("select--->" + select);
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                // 清除
                it.remove();
                // 判断
                if (Objects.nonNull(selectionKey)) {
                    if (selectionKey.isConnectable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        if (channel.isConnectionPending()) {
                            // 完成连接
                            channel.finishConnect();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            byteBuffer.put(String.format("%s连接成功", LocalDateTime.now()).getBytes());
                            byteBuffer.flip();
                            channel.write(byteBuffer);
                            executor.submit(() -> {
                                while (true) {
                                    byteBuffer.clear();
                                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                                    byteBuffer.put(br.readLine().getBytes());
                                    byteBuffer.flip();
                                    channel.write(byteBuffer);
                                }
                            });
                        }
                        // 注册读取事件
                        channel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        while (true) {
                            SocketChannel channel = (SocketChannel) selectionKey.channel();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            int len = channel.read(byteBuffer);
                            if (len > 0) {
                                System.out.println(String.valueOf(StandardCharsets.UTF_8.decode(byteBuffer).array()));
                            } else {
                                channel.close();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
