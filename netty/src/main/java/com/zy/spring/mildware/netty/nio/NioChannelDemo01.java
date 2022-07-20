package com.zy.spring.mildware.netty.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.SortedMap;

/*
 * 一、通道（Channel）：用于源节点与目标节点的连接。在 Java NIO 中负责缓冲区中数据的传输。Channel 本身不存储数据，因此需要配合缓冲区进行传输。
 *
 * 二、通道的主要实现类
 * 	java.nio.channels.Channel 接口：
 * 		|--FileChannel              用于读取、写入、映射和操作文件的通道。
 * 		|--SocketChannel            通过 TCP 读写网络中的数据
 * 		|--ServerSocketChannel      可以监听新进来的 TCP 连接，对每一个新进来的连接都会创建一个 SocketChannel
 * 		|--DatagramChannel          通过 UDP 读写网络中的数据通道
 *
 * 三、获取通道有三种方式
 * 1. Java 针对支持通道的类提供了 getChannel() 方法
 * 		本地 IO：
 * 		FileInputStream/FileOutputStream
 * 		RandomAccessFile
 *
 * 		网络IO：
 * 		Socket
 * 		ServerSocket
 * 		DatagramSocket
 *
 * 2. 在 JDK 1.7 中的 NIO.2 针对各个通道提供了静态方法 open()
 * 3. 在 JDK 1.7 中的 NIO.2 的 Files 工具类的 newByteChannel()
 *
 * 四、通道之间的数据传输
 * transferFrom()
 * transferTo()
 *
 * 五、分散(Scatter)与聚集(Gather)
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中
 *
 * 六、字符集：Charset
 * 编码：字符串 -> 字节数组
 * 解码：字节数组  -> 字符串
 *
 */
public class NioChannelDemo01 {

    // 6.指定字符集的编码与解码
    public void fn06() throws Exception {
        Charset charset = Charset.forName("utf-8");
        // 获取编码器
        CharsetEncoder encoder = charset.newEncoder();
        // 获取解码器
        CharsetDecoder decoder = charset.newDecoder();
        CharBuffer cBuffer = CharBuffer.allocate(1024);
        cBuffer.put("好好学习,天天向上");
        cBuffer.flip();
        // 编码
        ByteBuffer bBuffer = encoder.encode(cBuffer);
        // 解码
        bBuffer.flip();
        CharBuffer charBuffer = decoder.decode(bBuffer);
        System.out.println(charBuffer);

    }

    // 5.遍历所有支持的字符集
    public void fn05(){
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        charsets.forEach((k,v)->{
            System.out.println("item:"+k+"===============value:"+v);
        });
    }

    // 4.分散读取与聚集写入
    public void fn04() throws Exception{
        RandomAccessFile raf = new RandomAccessFile("E:/idea.txt", "rw");
        // 获取通道
        FileChannel channel = raf.getChannel();
        // 分配指定的缓冲区的大小
        ByteBuffer buffer = ByteBuffer.allocate(100);
        ByteBuffer buffer1 = ByteBuffer.allocate(1024);
        // 分散读取
        ByteBuffer[] buffers = {buffer, buffer1};
        channel.read(buffers);
        for (ByteBuffer bf : buffers){
            bf.flip();
        }
        System.out.println("==================分散读取开始====================");
        System.out.println(new String(buffers[0].array(), 0, buffers[0].limit()));
        System.out.println(new String(buffers[1].array(), 0, buffers[1].limit()));
        System.out.println("==================分散读取结束====================");

        // 聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("E:/idea01.txt", "rw");
        FileChannel channel1 = raf2.getChannel();
        channel1.write(buffers);

    }

    // 3.使用直接缓冲区完成文件的复制(较为简便的方式)
    public void fn03(){
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = FileChannel.open(Paths.get("E:/1.png"), StandardOpenOption.READ);
            out = FileChannel.open(Paths.get("E:/4.png"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

            // 方法一: in.transferTo(0, in.size(), out);
            // 方法二:
            out.transferFrom(in, 0, in.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 2.使用直接缓冲区完成文件的复制(内存映射文件)(较为复杂的方式,少用)
    /*建议将直接缓冲区主要分配给那些易受基础系统的本机 I/O 操作影响的大型、持久的缓冲区。
    一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好处时分配它们*/
    public void fn02(){
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = FileChannel.open(Paths.get("E:/1.png"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("E:/3.png"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
            // 内存映射文件,只支持byteBuffer
            MappedByteBuffer inMapBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMapBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
            //直接对缓冲区进行数据的读写操作
            byte[] b = new byte[inMapBuffer.limit()];
            inMapBuffer.get(b);
            outMapBuffer.put(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inChannel.close();
                outChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // 1.利用通道完成文件的复制（非直接缓冲区）
    public void fn01(){
        FileInputStream is = null;
        FileOutputStream os = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            is = new FileInputStream("E:/1.png");
            os = new FileOutputStream("E:/2.png");
            // 获取通道
            inChannel = is.getChannel();
            outChannel = os.getChannel();
            // 分配指定大小的缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 将通道中的数据存入缓冲区中
            while (inChannel.read(buffer) != -1){
                // 切换读取数据的模式
                buffer.flip();
                // 将缓冲区的数据写入通道中
                outChannel.write(buffer);
                // 清空缓冲区
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outChannel.close();
                inChannel.close();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
