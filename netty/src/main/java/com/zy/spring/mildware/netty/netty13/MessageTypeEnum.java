package com.zy.spring.mildware.netty.netty13;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageTypeEnum {
    /**
     * 请求
     */
    REQUEST((byte)1),
    RESPONSE((byte)2),
    PING((byte)3),
    PONG((byte)4),
    EMPTY((byte)5),
    ;

    private final byte type;

    public static MessageTypeEnum get(byte type) {
        for (MessageTypeEnum value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        throw new RuntimeException("unsupported type: " + type);
    }

}
