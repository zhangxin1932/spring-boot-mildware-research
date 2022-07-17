package com.zy.spring.mildware.netty.netty13;

public interface Resolver {
    boolean support(Message message);
    Message resolve(Message message);
}
