package com.zy.spring.mildware.misc.threadlocal;

public class JDKThreadLocal01 {

    private static final ThreadLocal<Integer> THREAD_LOCAL = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            THREAD_LOCAL.set(99);
            System.out.println(Thread.currentThread().getName() + " --> " + THREAD_LOCAL.get());
        },"t1");
        t1.start();
        t1.join();
        System.out.println(Thread.currentThread().getName() + " --> " + THREAD_LOCAL.get());
        THREAD_LOCAL.remove();
    }

}
