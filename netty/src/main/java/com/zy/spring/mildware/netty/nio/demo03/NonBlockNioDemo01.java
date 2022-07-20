package com.zy.spring.mildware.netty.nio.demo03;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/*
 * 一、使用 NIO 完成网络通信的三个核心：
 *
 * 1. 通道（Channel）：负责连接
 *
 * 	   java.nio.channels.Channel 接口：
 * 			|--SelectableChannel
 * 				|--SocketChannel
 * 				|--ServerSocketChannel
 * 				|--DatagramChannel
 *
 * 				|--Pipe.SinkChannel
 * 				|--Pipe.SourceChannel
 *
 * 2. 缓冲区（Buffer）：负责数据的存取
 *
 * 3. 选择器（Selector）：是 SelectableChannel 的多路复用器。用于监控 SelectableChannel 的 IO 状况
 *
 * 非阻塞式NIO
 */
public class NonBlockNioDemo01 {

    // 客户端
    public void client() throws IOException {
        // 1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9090));
        // 2.切换为非阻塞模式
        socketChannel.configureBlocking(false);
        // 3.分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 4.发送消息到服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();
            buffer.put((LocalDateTime.now().toString()+"\n"+str).getBytes());
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        // 5.关闭通道
        socketChannel.close();
    }

    // 服务端
    public void server() throws IOException {
        // 1.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2.切换为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 3.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9090));
        // 4.获取选择器
        Selector selector = Selector.open();
        // 5.将通道注册到选择器上,指定监听事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 6.轮询式的获取选择器上准备就绪的事件
        while (selector.select() > 0){
            // 7.获取当前选择器中所有注册的选择键(已就绪的监听事件)
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            // 8.迭代获取准备就绪的事件
            while (keyIterator.hasNext()){
                SelectionKey next = keyIterator.next();
                // 9.判断具体是什么事件准备就绪
                if (next.isAcceptable()){
                    // 10.若接收就绪,则获取客户端连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 11.切换为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 12.将该通道注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if (next.isReadable()){
                    // 获取读就绪通道
                    SocketChannel channel = (SocketChannel) next.channel();
                    // 读取数据
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = channel.read(buffer)) > 0){
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, len));
                        buffer.clear();
                    }
                }
                // 取消选择键
                keyIterator.remove();
            }
        }
    }
}
