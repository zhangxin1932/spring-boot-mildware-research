package com.zy.spring.mildware.netty.netty10.core;

/**
 * Message 参考 TCP 协议，将其抽象成由 Header 和 Payload 组成（即首部和数据块）。
 * 其中，报文的 Header 部分共 9 个字节，包含魔数、版本号、序列化方式、指令、SerialNo，结构如下：
 * +--------------+---------------+------------+---------------+-----------+
 * | 魔数(4) | version(1) |序列化方式(1) | command(1) |SerialNo(2)|
 * +--------------+---------------+------------+---------------+-----------+
 *
 *
 * @param <T>
 */
public abstract class Message<T extends MessageBody> {
    private MessageHeader messageHeader;
    private T messageBody;
    public T getMessageBody() {
        return messageBody;
    }
}