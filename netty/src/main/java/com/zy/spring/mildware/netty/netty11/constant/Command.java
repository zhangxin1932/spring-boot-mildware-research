package com.zy.spring.mildware.netty.netty11.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 协议指令枚举
 * </p>
 */
@AllArgsConstructor
@Getter
public enum Command {

    /**
     *
     */
    HANDSHAKE_REQ('1', "握手请求消息"),
    HANDSHAKE_RESP('2', "握手应答消息"),
    BIZ_REQ('3', "业务请求消息"),
    BIZ_RESP('4', "业务应答消息"),
    HEARTBEAT_REQ('5', "心跳请求消息"),
    HEARTBEAT_RESP('6', "心跳应答消息"),
    ;

    private final char val;
    private final String desc;

    public static Command of(char val){
        for (Command value : Command.values()) {
            if (value.getVal() == val){
                return value;
            }
        }
        return null;
    }

}
