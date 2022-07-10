package com.zy.spring.mildware.netty.netty10.core;

public class MessageHeader {
    /**
     * 魔数
     */
    private int magicNumber;
    /**
     * 版本号，当前协议的版本号为 1
     */
    private int version = 1;
    /**
     * 序列化方式，默认使用 json
     */
    private int serializeMethod;
    /**
     * 消息的指令
     */
    private int command;
    /**
     * 任务的流水号
     */
    private long serialNo;
}
