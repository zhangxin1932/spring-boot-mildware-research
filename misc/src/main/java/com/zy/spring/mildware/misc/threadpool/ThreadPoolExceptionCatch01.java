package com.zy.spring.mildware.misc.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.*;

public class ThreadPoolExceptionCatch01 {

    public static void main(String[] args) {
        /*ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
                // new LinkedBlockingQueue<>(1),
                new SynchronousQueue<>(),
                new DefaultThreadFactory("my-threadFactory"),
                new ThreadPoolExecutor.CallerRunsPolicy()) {
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                // fixme, 这里也可以解决 submit 不打印堆栈信息的问题
                t.printStackTrace();
            }
        };*/

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS,
                // new LinkedBlockingQueue<>(1),
                new SynchronousQueue<>(),
                new DefaultThreadFactory("my-threadFactory"),
                // customThreadFactory(), // fixme, 这里也可以解决 submit 不打印堆栈信息的问题
                new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 1; i < 10; i++) {
            // testExecute(threadPoolExecutor, finalI);
            // testSubmit(threadPoolExecutor, finalI);
            // testExecuteCatchThrowable(threadPoolExecutor, finalI);
            testSubmitCatchThrowable(threadPoolExecutor, i);
        }
    }

    /**
     * 执行 execute 方法(任务内部不捕获异常)
     * @param threadPoolExecutor
     * @param finalI
     */
    private static void testExecute(ThreadPoolExecutor threadPoolExecutor, int finalI) {
        // fixme, execute 方法 ==> 执行时抛出了异常, 此处会打印堆栈信息
        threadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " ===> " + finalI);
            throw new RuntimeException(Thread.currentThread().getName() + " ===> " + "error --》 " + finalI);
        });
    }

    /**
     * 执行 submit 方法(任务内部不捕获异常)
     * @param threadPoolExecutor
     * @param finalI
     */
    private static void testSubmit(ThreadPoolExecutor threadPoolExecutor, int finalI) {
        // fixme, submit 方法 ==> 执行时抛出了异常, 此处不会打印堆栈信息
        threadPoolExecutor.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " ===> " + finalI);
            throw new RuntimeException(Thread.currentThread().getName() + " ===> " + "error --》 " + finalI);
        });
    }

    /**
     * 执行 execute 方法(任务内部捕获异常)
     * @param threadPoolExecutor
     * @param finalI
     */
    private static void testExecuteCatchThrowable(ThreadPoolExecutor threadPoolExecutor, int finalI) {
        threadPoolExecutor.execute(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ===> " + finalI);
                throw new RuntimeException(Thread.currentThread().getName() + " ===> " + "error --》 " + finalI);
            } catch (Throwable e) {
                // fixme, 任务内部捕获所有异常, 打印堆栈信息
                e.printStackTrace();
            }
        });
    }

    /**
     * 执行 execute 方法(任务内部捕获异常)
     * @param threadPoolExecutor
     * @param finalI
     */
    private static void testSubmitCatchThrowable(ThreadPoolExecutor threadPoolExecutor, int finalI) {
        // fixme, execute 方法 ==> 执行时抛出了异常
        threadPoolExecutor.submit(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " ===> " + finalI);
                throw new RuntimeException(Thread.currentThread().getName() + " ===> " + "error --》 " + finalI);
            } catch (Throwable e) {
                // fixme, 任务内部捕获所有异常, 打印堆栈信息
                e.printStackTrace();
            }
        });
    }

    private static ThreadFactory customThreadFactory() {
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
            // fixme, 这里可以打印各种堆栈信息及日志
            e.printStackTrace();
        };

        return new ThreadFactoryBuilder()
                .setNameFormat("custom-threadFactory")
                .setUncaughtExceptionHandler(uncaughtExceptionHandler)
                .build();
    }

}
