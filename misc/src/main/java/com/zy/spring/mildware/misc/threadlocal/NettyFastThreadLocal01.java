package com.zy.spring.mildware.misc.threadlocal;

import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.concurrent.FastThreadLocalThread;

public class NettyFastThreadLocal01 {
    private static final FastThreadLocal<Integer> FAST_THREAD_LOCAL = new FastThreadLocal<>();

    public static void main(String[] args) {

        // 1.这里使用 netty 的 FastThreadLocalThread, 在线程结束时, 不需要手动 remove
        // 因为 FastThreadLocalThread 包装了线程 FastThreadLocalRunnable, 在其 run 方法的 finally 块中 removeAll 了
        new FastThreadLocalThread(() -> {
            for (int i = 0; i < 10; i++) {
                FAST_THREAD_LOCAL.set(i);
                System.out.println(Thread.currentThread().getName() + " --> " + FAST_THREAD_LOCAL.get());
            }
        }, "t1").start();

        // 2.如果是普通线程，还是应该最佳实践: 在 finally 块中 remove
        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    FAST_THREAD_LOCAL.set(i);
                    System.out.println(Thread.currentThread().getName() + " --> " + FAST_THREAD_LOCAL.get());
                }
            } finally {
                FAST_THREAD_LOCAL.remove();
            }
        }, "t2").start();
    }
}
