package com.zy.spring.mildware.netty.nio.demo04;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * UDP-NIO
 */
public class NonBlockNioDemo02 {

    // 客户端
    public void client() throws IOException {
        // 1.获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 2.切换为非阻塞模式
        datagramChannel.configureBlocking(false);
        // 3.分配指定的缓冲区大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 4.发送消息到服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String next = scanner.next();
            buffer.put((LocalDateTime.now().toString()+"\n"+next).getBytes());
            buffer.flip();
            datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1",9090));
            buffer.clear();
        }
        // 5.关闭资源
        datagramChannel.close();
    }

    // 服务端
    public void server() throws IOException {
        // 1.获取通道
        DatagramChannel datagramChannel = DatagramChannel.open();
        // 2.切换为nio状态
        datagramChannel.configureBlocking(false);
        // 3.绑定连接
        datagramChannel.bind(new InetSocketAddress(9090));
        // 4.获得选择器
        Selector selector = Selector.open();
        // 5.将通道注册到选择器上,并绑定监听事件
        datagramChannel.register(selector, SelectionKey.OP_READ);
        // 6.通过轮询方式获取选择器上准备就绪的事件
        while (selector.select() > 0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey next = it.next();
                if (next.isReadable()){
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    datagramChannel.receive(buffer);
                    buffer.flip();
                    System.out.println(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            it.remove();
        }
    }

}
