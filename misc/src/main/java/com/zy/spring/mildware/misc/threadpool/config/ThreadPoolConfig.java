package com.zy.spring.mildware.misc.threadpool.config;

import com.zy.spring.mildware.misc.threadpool.common.PoolName;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.*;

/**
 * https://www.cnblogs.com/warehouse/p/10732965.html
 * https://tech.meituan.com/2020/04/02/java-pooling-pratice-in-meituan.html
 */
@Configuration
public class ThreadPoolConfig {

    private static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
    public static final ConcurrentMap<String, ThreadPoolExecutor> EXECUTOR_MAP = new ConcurrentHashMap<>();

    public static final String REFUND_THREAD_POOL_EXECUTOR = "refundThreadPoolExecutor";
    public static final String STU_THREAD_POOL_EXECUTOR = "stuThreadPoolExecutor";
    public static final String TEACHER_THREAD_POOL_EXECUTOR = "teacherThreadPoolExecutor";

    @Bean(name = REFUND_THREAD_POOL_EXECUTOR)
    public BlockedAndFixedThreadPoolExecutor refundThreadPoolExecutor() {
        BlockedAndFixedThreadPoolExecutor executor = new BlockedAndFixedThreadPoolExecutor(PROCESSORS, 10, "refund");
        EXECUTOR_MAP.put(PoolName.refund_executor.name(), executor);
        return executor;
    }

    @Bean(name = STU_THREAD_POOL_EXECUTOR)
    public ThreadPoolExecutor stuThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
                2,
                0L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new DefaultThreadFactory(PoolName.stu_executor.name()),
                new ThreadPoolExecutor.AbortPolicy());
        EXECUTOR_MAP.put(PoolName.stu_executor.name(), executor);
        return executor;
    }

    @Bean(name = TEACHER_THREAD_POOL_EXECUTOR)
    public ThreadPoolExecutor teacherThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(PROCESSORS * 2,
                PROCESSORS * 2,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                new DefaultThreadFactory(PoolName.teacher_executor.name()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        EXECUTOR_MAP.put(PoolName.teacher_executor.name(), executor);
        return executor;
    }
}
