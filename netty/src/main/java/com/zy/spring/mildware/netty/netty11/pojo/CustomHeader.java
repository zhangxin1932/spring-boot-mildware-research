package com.zy.spring.mildware.netty.netty11.pojo;

import com.zy.spring.mildware.netty.netty11.constant.Const;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 协议头
 * </p>
 */
@Data
public class CustomHeader {

    /**
     * 协议魔数，固定值 0xACAFDCBA
     */
    private int major = Const.MAJOR;
    /**
     * 协议主版本：
     */
    private char mainVersion = Const.MAIN_VERSION;
    /**
     * 协议次版本：
     */
    private char minorVersion = Const.MINOR_VERSION;
    /**
     * 消息总长度
     */
    private int length;
    /**
     * 会话id
     */
    private long sid;
    /**
     * 指令
     */
    private char command;
    /**
     * 是否成功  1：成功，0：失败
     */
    private int success;
    /**
     * 消息头扩展属性
     */
    private Map<String, Object> attribute = new HashMap<>();

}
