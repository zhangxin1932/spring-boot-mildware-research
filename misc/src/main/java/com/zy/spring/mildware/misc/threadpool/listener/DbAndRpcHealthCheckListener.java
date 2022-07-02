package com.zy.spring.mildware.misc.threadpool.listener;

import com.zy.spring.mildware.misc.threadpool.config.BlockedAndFixedThreadPoolExecutor;
import com.zy.spring.mildware.misc.threadpool.config.RefundWorker;
import com.zy.spring.mildware.misc.threadpool.config.ThreadPoolConfig;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 应保证确定应用启动后修改此状态. 比如初始化后延迟 10min 后调度.
 * 避免阻塞应用的启动过程
 */
public class DbAndRpcHealthCheckListener implements SpringApplicationRunListener {

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("dbHealthCheckWorker"));

    public static final AtomicBoolean DB_ALIVE = new AtomicBoolean(false);
    public static final AtomicBoolean RPC_ALIVE = new AtomicBoolean(false);

    private BlockedAndFixedThreadPoolExecutor executor;

    public DbAndRpcHealthCheckListener(SpringApplication application, String[] args) {
    }

    /**
     * 一定要 catch 异常
     * @param context
     */
    @Override
    public void running(ConfigurableApplicationContext context) {
        this.executor = (BlockedAndFixedThreadPoolExecutor) context.getBean(ThreadPoolConfig.REFUND_THREAD_POOL_EXECUTOR);
        execute();
    }

    private void execute() {
        try {
            System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS").format(LocalDateTime.now()) + "----> 这里执行 db 健康检查的任务, 根据健康检查的情况, 修改 dbAlive 状态.");
            DB_ALIVE.set(true);
            RPC_ALIVE.set(true);
            if (DB_ALIVE.get() && RPC_ALIVE.get()) {
                executor.signalAll();
                RefundWorker.signalAll();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            scheduledExecutorService.schedule(this::execute, 5L, TimeUnit.SECONDS);
        }
    }

}
