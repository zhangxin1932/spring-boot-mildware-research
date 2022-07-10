package com.zy.spring.mildware.netty.netty10.core;


/**
 * 在业务系统中，我们通常需要定义很多个指令，一个指令对应一个 Packet。
 * Header 的 command 字段用来区分不同的指令。
 * 在 Packet 的 Header 中，command 定义了 1 个字节，表示它支持 256 种指令。
 * 所以，我们可以定义一个最多包含 256 个指令的指令集 Commands，其定义方式如下：
 *
 * 当然，如果觉得 256 个指令不够，修改协议 Header 中 command 的字节数即可。
 */
public interface Command {
    /**
     * 心跳包
     */
    Byte HEART_BEAT = 0;
    /**
     * 登录请求
     */
    Byte LOGIN_REQUEST = 1;
    /**
     * 登录响应
     */
    Byte LOGIN_RESPONSE = 2;
    /**
     * 消息请求
     */
    Byte MESSAGE_REQUEST = 3;
    /**
     * 消息响应
     */
    Byte MESSAGE_RESPONSE = 4;
}
