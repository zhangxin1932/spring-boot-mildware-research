package com.zy.spring.mildware.netty.nio.demo01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
 */
public class BlockNioDemo01 {

    // 客户端
    public void client() throws IOException {
        // 1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));
        FileChannel inChannel = FileChannel.open(Paths.get("E:/1.png"), StandardOpenOption.READ);
        // 2.分配指定的缓冲区的大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 3.读取本地文件,并发送到服务端
        while (inChannel.read(buffer) != -1){
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        // 4.关闭通道
        inChannel.close();
        socketChannel.close();
    }

    // 服务端
    public void server() throws IOException {
        // 1.获取通道
        ServerSocketChannel channel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("E:/5.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        // 2.绑定连接
        channel.bind(new InetSocketAddress(8888));
        // 3.获取客户端连接的通道
        SocketChannel clientChannel = channel.accept();
        // 4.分配指定的缓冲区的大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 5.接收客户端的数据并保存到本地
        while (clientChannel.read(buffer) != -1){
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }
        // 6.关闭资源
        clientChannel.close();
        outChannel.close();
        channel.close();

    }

}
