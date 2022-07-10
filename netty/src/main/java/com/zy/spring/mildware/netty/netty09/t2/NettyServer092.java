package com.zy.spring.mildware.netty.netty09.t2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.charset.StandardCharsets;

/**
 * https://blog.csdn.net/mzjnumber1/article/details/106663193
 *
 * 1.{@link LengthFieldBasedFrameDecoder}
 *
 * 该解码器根据消息中的 length 字段的值动态拆分接收到的 ByteBuf，
 * 在解码二进制报文（包含报文头：报文头存储报文的长度）时，此解码器特别有用
 * 一般配合 {@link LengthFieldPrepender} 使用
 *
 * 2.LengthFieldBasedFrameDecoder 构造器参数解析 :
 * 2.1 byteOrder
 * 表示 Length 字段的字节是大端还是小端
 *
 * 2.2 maxFrameLength
 * 表示最大长度
 *
 * 2.3 lengthFieldOffset
 * 表示 Length 字段的偏移量（如果之前还有其他报文，没有为 0），其实可以理解为一个 Header
 *
 * 2.4 lengthFieldLength
 * Length 字段占用的字节数, 意思是真实内容的字节长度
 * 如果 lengthFieldLength 是 1 个字节，那么限制为 128 bytes；
 * 如果 lengthFieldLength 是 2 个字节，那么限制为 32767 bytes (约等于 32K)；
 * 如果 lengthFieldLength 是 3 个字节，那么限制为 8388608 bytes (约等于 8M)；
 * 如果 lengthFieldLength 是 4 个字节，那么限制为 2147483648 bytes (约等于 2G)。
 *
 * 2.5 lengthAdjustment
 * Length 字段补偿值, 可以理解为第二个Header
 *
 * 2.6 initialBytesToStrip
 * 跳过初始字节数 获取真正的内容, 意思是从字节流开始，跳过多少个字节
 *
 * 2.7 failFast
 * 就是超出 maxFrameLength 是立马抛出还是读完数据再抛出
 *
 * 3.常见案例: 报文只包含Length字段和Content字段
 * +--------+----------+
 * | Length |  Content |
 * +--------+----------+
 *
 * 3.1 案例1：
 * lengthFieldOffset = 0 // 因为报文以Length字段开始，不需要跳过任何字节，所以offset为0
 * lengthFieldLength = 2 // 因为我们规定Length字段占用字节数为2，所以这个字段值传入的是2
 * lengthAdjustment = 0 // 这里Length字段值不需要补偿，因此设置为0
 * initialBytesToStrip = 0 // 不跳过初始字节，意味着解码后的 ByteBuf 中，包含 Length+Content 所有内容
 *
 * 解码前 (14 bytes)                 解码后 (14 bytes)
 *    +--------+----------------+      +--------+----------------+
 *    | Length | Actual Content |----->| Length | Actual Content |
 *    | 0x000C | "HELLO, WORLD" |      | 0x000C | "HELLO, WORLD" |
 *    +--------+----------------+      +--------+----------------+
 *
 * 3.2 案例2：
 * lengthFieldOffset = 0 // 参见案例1
 * lengthFieldLength = 2 // 参见案例1
 * lengthAdjustment = 0 // 参见案例1
 * initialBytesToStrip = 2 // 这里跳过2个初始字节，也就是Length字段占用的字节数，意味着解码后的ByteBuf中，只包含Content字段
 *
 *    BEFORE DECODE (14 bytes)         AFTER DECODE (12 bytes)
 *    +--------+----------------+      +----------------+
 *    | Length | Actual Content |----->| Actual Content |
 *    | 0x000C | "HELLO, WORLD" |      | "HELLO, WORLD" |
 *    +--------+----------------+      +----------------+
 *
 * 3.3 案例3：
 * lengthFieldOffset = 0 // 参见案例1
 * lengthFieldLength = 2 // 参见案例1
 * lengthAdjustment = -2 // Length字段补偿值指定为-2
 * initialBytesToStrip = 0 // 参见案例1
 *
 *  BEFORE DECODE (14 bytes)         AFTER DECODE (14 bytes)
 *    +--------+----------------+      +--------+----------------+
 *    | Length | Actual Content |----->| Length | Actual Content |
 *    | 0x000E | "HELLO, WORLD" |      | 0x000E | "HELLO, WORLD" |
 *    +--------+----------------+      +--------+----------------+
 * 这个案例需要进行一下特殊说明，其Length字段值表示：
 * Length字段本身占用的字节数+Content字节数。
 * 所以我们看到解码前，其值为0x000E(14)，而不是0x000C(12)。
 * 而真实Content字段内容只有2个字节，因此我们需要用：
 * Length字段值0x000E(14)，减去lengthAdjustment指定的值(-2)，表示的才是Content字段真实长度。
 *
 *
 * 4.常见案例: 报文头包含Length字段以外的其他字段，同时包含Content字段
 * 通常情况下，一个协议的报文头除了Length字段，还会包含一些其他字段，
 * 例如协议的版本号，采用的序列化协议，是否进行了压缩，甚至还会包含一些预留的头字段，以便未来扩展。
 * 这些字段可能位于Length之前，也可能位于Length之后，此时的报文协议格式如下所示：
 * +---------+--------+----------+----------+
 * |........ | Length |  ....... |  Content |
 * +---------+--------+----------+----------+
 * 当然，对于LengthFieldBasedFrameDecoder来说，其只关心Length字段。
 * 按照Length字段的值解析出一个完整的报文放入ByteBuf中，也就是说，
 * LengthFieldBasedFrameDecoder只负责粘包、半包的处理，
 * 而ByteBuf中的实际内容解析，则交由后续的解码器进行处理。
 *
 * 4.1 案例4：
 * 这个案例中，在Length字段之前，还包含了一个Header字段，其占用2个字节，Length字段占用3个字节。
 * lengthFieldOffset = 2 // 需要跳过Header字段占用的2个字节，才是Length字段
 * lengthFieldLength = 3 // Length字段占用3个字节
 * lengthAdjustment = 0 //由于Length字段的值为12，表示的是Content字段长度，因此不需要调整
 * initialBytesToStrip = 0 //解码后，不裁剪字节
 *    BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
 *    +----------+----------+----------------+      +----------+----------+----------------+
 *    | Header   |  Length  | Actual Content |----->| Header   |  Length  | Actual Content |
 *    |  0xCAFE  | 0x00000C | "HELLO, WORLD" |      |  0xCAFE  | 0x00000C | "HELLO, WORLD" |
 *    +----------+----------+----------------+      +----------+----------+----------------+
 *
 * 4.2 案例5：
 * 在这个案例中，Header字段位于Length字段之后
 * lengthFieldOffset = 0 // 由于一开始就是Length字段，因此不需要跳过
 * lengthFieldLength = 3 // Length字段占用3个字节，其值为0x000C，表示Content字段长度
 * lengthAdjustment = 2 // 由于Length字段之后，还有Header字段，因此需要+2个字节，读取Header+Content的内容
 * initialBytesToStrip = 0 //解码后，不裁剪字节
 *   BEFORE DECODE (17 bytes)                      AFTER DECODE (17 bytes)
 *    +----------+----------+----------------+      +----------+----------+----------------+
 *    |  Length  | Header   | Actual Content |----->|  Length  | Header   | Actual Content |
 *    | 0x00000C |  0xCAFE  | "HELLO, WORLD" |      | 0x00000C |  0xCAFE  | "HELLO, WORLD" |
 *    +----------+----------+----------------+      +----------+----------+----------------+
 *
 * 4.3 案例6 :
 * 这个案例中，Length字段前后各有一个报文头字段HDR1、HDR2，各占1个字节
 * lengthFieldOffset = 1 //跳过HDR1占用的1个字节读取Length
 * lengthFieldLength = 2 //Length字段占用2个字段，其值为0x000C(12)，表示Content字段长度
 * lengthAdjustment = 1 //由于Length字段之后，还有HDR2字段，因此需要+1个字节，读取HDR2+Content的内容
 * initialBytesToStrip = 3 //解码后，跳过前3个字节
 *    BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
 *    +------+--------+------+----------------+      +------+----------------+
 *    | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
 *    | 0xCA | 0x000C | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
 *    +------+--------+------+----------------+      +------+----------------+
 *
 * 4.4 案例7：
 * //这个案例中，Length字段前后各有一个报文头字段HDR1、HDR2，各占1个字节。Length占用2个字节，表示的是整个报文的总长度。
 * lengthFieldOffset = 1 //跳过HDR1占用的1个字节读取Length
 * lengthFieldLength = 2 //Length字段占用2个字段，其值为0x0010(16)，表示HDR1+Length+HDR2+Content长度
 * lengthAdjustment = -3 //由于Length表示的是整个报文的长度，减去HDR1+Length占用的3个字节后，读取HDR2+Content长度
 * initialBytesToStrip = 3 //解码后，跳过前3个字节
 *  BEFORE DECODE (16 bytes)                       AFTER DECODE (13 bytes)
 *    +------+--------+------+----------------+      +------+----------------+
 *    | HDR1 | Length | HDR2 | Actual Content |----->| HDR2 | Actual Content |
 *    | 0xCA | 0x0010 | 0xFE | "HELLO, WORLD" |      | 0xFE | "HELLO, WORLD" |
 *    +------+--------+------+----------------+      +------+----------------+
 *
 *
 *
 * @author zx
 */
public class NettyServer092 {

    public static void main(String[] args) {
        ServerBootstrap server = new ServerBootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            channel.pipeline()
                                    .addLast(new LengthFieldBasedFrameDecoder (102400,0,4,0, 2))
                                    .addLast(new LengthFieldPrepender(4))
                                    .addLast(new StringDecoder())
                                    .addLast(new NettyServerHandler092());
                        }
                    });
            ChannelFuture future = server.bind("127.0.0.1", 8099).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static class NettyServerHandler092 extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println("请求报文体 = " + msg);

            byte[] response = "你好, 中国, 我是服务端".getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeBytes(response);
            channelHandlerContext.writeAndFlush(buffer);
        }
    }

}
