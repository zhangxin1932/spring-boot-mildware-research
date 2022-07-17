package com.zy.spring.mildware.netty.netty13;

import java.util.concurrent.atomic.LongAdder;

public class SessionIdGenerator {
    private static final LongAdder ID = new LongAdder();

    public static String generate() {
        ID.increment();
        return String.valueOf(ID.longValue());
    }
}
