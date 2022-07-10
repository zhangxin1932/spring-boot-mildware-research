package com.zy.spring.mildware.netty.netty11.pojo;

import lombok.Data;

/**
 * <p>
 * 协议消息
 * </p>
 */
@Data
public class CustomMessage {

    private CustomHeader header;
    private Object body;

}
