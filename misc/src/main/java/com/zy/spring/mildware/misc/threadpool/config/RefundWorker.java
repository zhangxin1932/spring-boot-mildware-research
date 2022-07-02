package com.zy.spring.mildware.misc.threadpool.config;

import com.zy.spring.mildware.misc.threadpool.listener.DbAndRpcHealthCheckListener;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class RefundWorker implements Callable<Void> {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition CONDITION = LOCK.newCondition();

    @Override
    public Void call() throws Exception {
        LOCK.lock();
        try {
            while (!DbAndRpcHealthCheckListener.DB_ALIVE.get() || !DbAndRpcHealthCheckListener.RPC_ALIVE.get()) {
                CONDITION.await(3L, TimeUnit.SECONDS);
            }
            System.out.println("RefundWorker 执行任务");
            return null;
        } catch (Throwable e) {
            log.warn("failed to execute task.", e);
            return null;
        } finally {
            LOCK.unlock();
        }
    }

    public static void signalAll() {
        LOCK.lock();
        try {
            CONDITION.signalAll();
        } finally {
            LOCK.unlock();
        }
    }
}
