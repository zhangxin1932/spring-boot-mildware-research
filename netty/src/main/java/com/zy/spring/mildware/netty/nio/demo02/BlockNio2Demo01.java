package com.zy.spring.mildware.netty.nio.demo02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * 阻塞式NIO2
 */
public class BlockNio2Demo01 {

    // 客户端
    public void client() throws IOException {
        // 1.获取通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        FileChannel fileChannel = FileChannel.open(Paths.get("E:/1.png"), StandardOpenOption.READ);
        // 2.分配指定的缓冲区的大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 3.读取本地文件,并发送到服务端
        while (fileChannel.read(buffer) != -1){
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
        socketChannel.shutdownOutput();
        // 4.接收服务端的反馈
        int len = 0;
        while ((len = socketChannel.read(buffer)) != -1){
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, len));
            buffer.clear();
        }
        // 5.关闭资源
        fileChannel.close();
        socketChannel.close();
    }

    // 服务端
    public void server() throws IOException {
        // 1.获取通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        FileChannel fileChannel = FileChannel.open(Paths.get("E:/6.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        // 2.绑定连接
        serverSocketChannel.bind(new InetSocketAddress(9999));
        // 3.获取客户端连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 4.分配指定缓冲区的大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 5.将客户端的资源保存到本地
        while (socketChannel.read(buffer) != -1){
            buffer.flip();
            fileChannel.write(buffer);
            buffer.clear();
        }
        // 6.发送反馈给客户端
        buffer.put("服务端数据接收成功".getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        // 7.关闭资源
        socketChannel.close();
        fileChannel.close();
        serverSocketChannel.close();

    }
}
