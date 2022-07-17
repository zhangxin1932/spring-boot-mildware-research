package com.zy.spring.mildware.netty.nio;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Random;

public class NioTest {
    public void fn01() {
        IntBuffer buffer = IntBuffer.allocate(7);

        for (int i = 0; i < buffer.capacity() - 2; i++) {
            buffer.put(new Random().nextInt());
        }

        System.out.println("before flip, limit is: " + buffer.limit());
        buffer.flip();
        System.out.println("after flip, limit is: " + buffer.limit());

        while (buffer.hasRemaining()) {
            System.out.println("----------------------");
            System.out.println("position: " + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());
            System.out.println(buffer.get());
        }
    }

    public void fn02() {
        try (FileInputStream fis = new FileInputStream("/input.txt");
             FileOutputStream fos = new FileOutputStream("/output.txt");
             FileChannel in = fis.getChannel();
             FileChannel out = fos.getChannel()) {
            // ByteBuffer buffer = ByteBuffer.allocate(1024); // new HeapByteBuffer(capacity, capacity);
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024); // new DirectByteBuffer(capacity);
            int len;
            while ((len = in.read(buffer)) != -1) {
                System.out.println("len value is: " + len); // 若没有调用 buffer.clear() 方法, 这里的 len 将是0
                buffer.flip();
                out.write(buffer);
                // buffer.clear(); // 此行代码若注释掉, 将从头重复向文件中写数据
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fn03() throws IOException {
        ServerSocketChannel open = ServerSocketChannel.open();
        open.socket().bind(new InetSocketAddress(8090));

        int msgLength = 2 + 3 + 4;
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel channel = open.accept();
        while (true) {
            // 读取外部输入
            long byteRead = 0;
            while (byteRead < msgLength) {
                long read = channel.read(buffers);
                byteRead += read;
                System.out.println("byteRead: " + byteRead);
                Arrays.stream(buffers).map(buffer -> "position: " + buffer.position() + "; limit: " + buffer.limit() + "; capacity: " + buffer.capacity())
                        .forEach(System.out::println);
            }
            Arrays.stream(buffers).forEach(Buffer::flip);

            // 向外部写信息
            long byteWrite = 0;
            while (byteWrite < msgLength) {
                long write = channel.write(buffers);
                byteWrite += write;
            }
            Arrays.stream(buffers).forEach(Buffer::clear);
        }
    }
}
