package com.zy.spring.mildware.misc.threadpool.config;

import com.zy.spring.mildware.misc.threadpool.listener.DbAndRpcHealthCheckListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class BlockedAndFixedThreadPoolExecutor extends ThreadPoolExecutor {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = this.lock.newCondition();
    private static final int BLOCKING_QUEUE_SIZE = 1024 * 2;

    @Getter
    private final int blockingQueueSize;

    public BlockedAndFixedThreadPoolExecutor(int poolSize, int blockingQueueSize, String threadPrefix) {
        super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(blockingQueueSize <= 0 ? BLOCKING_QUEUE_SIZE : blockingQueueSize), new ThreadFactory() {
            private final LongAdder threadNo = new LongAdder();

            @Override
            public Thread newThread(Runnable r) {
                threadNo.increment();
                String threadName = threadPrefix + threadNo.longValue();
                Thread thread = new Thread(r, threadName);
                thread.setDaemon(true);
                return thread;
            }
        });
        this.blockingQueueSize = blockingQueueSize <= 0 ? BLOCKING_QUEUE_SIZE : blockingQueueSize;
    }

    @Override
    public void execute(Runnable command) {
        this.lock.lock();
        try {
            while (!DbAndRpcHealthCheckListener.DB_ALIVE.get() || !DbAndRpcHealthCheckListener.RPC_ALIVE.get()) {
                this.condition.await(3L, TimeUnit.SECONDS);
            }
            super.execute(command);
        } catch (Throwable e) {
            log.warn("failed to execute task.", e);
        } finally {
            this.lock.unlock();
        }
    }

    public void signalAll() {
        this.lock.lock();
        try {
            this.condition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

}
