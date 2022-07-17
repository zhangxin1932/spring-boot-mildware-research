package com.zy.spring.mildware.netty.nio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NioClient01 {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(true);

        String fileName = "/input.txt";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        //  transferFrom 表示从...处读取... fileChannel.transferFrom()
        //  transferTo 表示向 ... 处写 ...
        long begin = System.currentTimeMillis();
        long transferByteCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        long end = System.currentTimeMillis();
        System.out.println(String.format("实际传递的字节数count: %s, 耗时: %sms", transferByteCount, (end - begin)));
        fileChannel.close();
        socketChannel.close();
    }
}
