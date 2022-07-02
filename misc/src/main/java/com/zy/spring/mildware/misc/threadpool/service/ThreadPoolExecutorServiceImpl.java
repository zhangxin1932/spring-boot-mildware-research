package com.zy.spring.mildware.misc.threadpool.service;

import com.zy.spring.mildware.misc.threadpool.config.ThreadPoolConfig;
import com.zy.spring.mildware.misc.threadpool.dto.ThreadPoolExecutorDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 这里的查询方法, 需要前端页面的配合:
 * 方案1: 前端页面定时器定时调用后端查询线程池状态的接口, 并更新状态
 * 方案2: 前端页面与后端接口采用 websocket 通信
 */
@Service
public class ThreadPoolExecutorServiceImpl {

    public ThreadPoolExecutorDTO getThreadPoolExecutorByName(String poolName) {
        ThreadPoolExecutor executor = ThreadPoolConfig.EXECUTOR_MAP.get(poolName);
        if (executor == null) {
            return null;
        }
        return buildThreadPoolExecutorDTO(poolName, executor);
    }

    public List<ThreadPoolExecutorDTO> getAllThreadPoolExecutors() {
        List<ThreadPoolExecutorDTO> list = new ArrayList<>();
        if (ThreadPoolConfig.EXECUTOR_MAP.size() > 0) {
            ThreadPoolConfig.EXECUTOR_MAP.forEach((k, v) -> list.add(buildThreadPoolExecutorDTO(k, v)));
        }
        return list;
    }

    public Boolean updateThreadPoolByName(ThreadPoolExecutorDTO threadPoolExecutorDTO) {
        ThreadPoolExecutor executor = ThreadPoolConfig.EXECUTOR_MAP.get(threadPoolExecutorDTO.getPoolName());
        if (executor == null) {
            return false;
        }
        Integer maximumPoolSize = threadPoolExecutorDTO.getMaximumPoolSize();
        if (Objects.nonNull(maximumPoolSize) && maximumPoolSize > 0) {
            executor.setMaximumPoolSize(maximumPoolSize);
        }
        Integer corePoolSize = threadPoolExecutorDTO.getCorePoolSize();
        if (Objects.nonNull(corePoolSize) && corePoolSize > 0) {
            executor.setCorePoolSize(corePoolSize);
        }
        Long keepAliveTime = threadPoolExecutorDTO.getKeepAliveTime();
        Boolean allowCoreThreadTimeOut = threadPoolExecutorDTO.getAllowCoreThreadTimeOut();
        if (Objects.nonNull(keepAliveTime)) {
            executor.setKeepAliveTime(keepAliveTime, TimeUnit.SECONDS);
            if (keepAliveTime > 0 && Objects.nonNull(allowCoreThreadTimeOut)) {
                executor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
            }
        }
        return true ;
    }

    private ThreadPoolExecutorDTO buildThreadPoolExecutorDTO(String poolName, ThreadPoolExecutor executor) {
        if (Objects.isNull(executor)) {
            return null;
        }
        BlockingQueue<Runnable> queue = executor.getQueue();
        return ThreadPoolExecutorDTO.builder()
                .poolName(poolName)
                .corePoolSize(executor.getCorePoolSize())
                .maximumPoolSize(executor.getMaximumPoolSize())
                .largestPoolSize(executor.getLargestPoolSize())
                .poolSize(executor.getPoolSize())
                .queueSize(queue.size())
                .queueRemainingCapacity(queue.remainingCapacity())
                .queueType(queue.getClass().getSimpleName())
                .keepAliveTime(executor.getKeepAliveTime(TimeUnit.SECONDS))
                .completedTaskCount(executor.getCompletedTaskCount())
                .activeCount(executor.getActiveCount())
                .taskCount(executor.getTaskCount())
                .allowCoreThreadTimeOut(executor.allowsCoreThreadTimeOut())
                .rejectHandlerClassName(executor.getRejectedExecutionHandler().getClass().getSimpleName())
                .build();
    }

}
