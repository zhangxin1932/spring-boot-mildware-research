package com.zy.spring.mildware.misc.threadpool.dto;

import java.io.Serializable;

public class ThreadPoolExecutorDTO implements Serializable {
    /**
     * 线程名前缀
     */
    private String poolName;
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maximumPoolSize;
    /**
     * 线程池中曾经启动的线程数的最大值, largestPoolSize <= maximumPoolSize
     */
    private Integer largestPoolSize;
    /**
     * 线程池中当前线程数量
     */
    private Integer poolSize;
    /**
     * 阻塞队列中任务数量
     */
    private Integer queueSize;
    /**
     * 阻塞队列剩余容量
     */
    private Integer queueRemainingCapacity;
    /**
     * 阻塞队列类型
     */
    private String queueType;
    /**
     * 空闲线程的存活时间: 单位: s
     */
    private Long keepAliveTime;
    /**
     * 已完成执行的近似任务总数: 近似值
     */
    private Long completedTaskCount;
    /**
     * 正在执行任务的线程数: 近似值
     */
    private Integer activeCount;
    /**
     * 线程池已执行和未执行的任务总数
     */
    private Long taskCount;
    /**
     * 拒绝策略
     */
    private String rejectHandlerClassName;
    /**
     * 是否允许核心线程数超时被销毁
     * true: 允许核心线程数超过 keepAliveTime 后被销毁
     * false: 不允许核心线程数被销毁
     */
    private Boolean allowCoreThreadTimeOut;

    public ThreadPoolExecutorDTO() {
    }

    public ThreadPoolExecutorDTO(String poolName, Integer corePoolSize, Integer maximumPoolSize, Integer largestPoolSize, Integer poolSize, Integer queueSize, Integer queueRemainingCapacity, String queueType, Long keepAliveTime, Long completedTaskCount, Integer activeCount, Long taskCount, String rejectHandlerClassName, Boolean allowCoreThreadTimeOut) {
        this.poolName = poolName;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.largestPoolSize = largestPoolSize;
        this.poolSize = poolSize;
        this.queueSize = queueSize;
        this.queueRemainingCapacity = queueRemainingCapacity;
        this.queueType = queueType;
        this.keepAliveTime = keepAliveTime;
        this.completedTaskCount = completedTaskCount;
        this.activeCount = activeCount;
        this.taskCount = taskCount;
        this.rejectHandlerClassName = rejectHandlerClassName;
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public static ThreadPoolExecutorDTOBuilder builder() {
        return new ThreadPoolExecutorDTOBuilder();
    }

    public String getPoolName() {
        return this.poolName;
    }

    public Integer getCorePoolSize() {
        return this.corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public Integer getLargestPoolSize() {
        return this.largestPoolSize;
    }

    public Integer getPoolSize() {
        return this.poolSize;
    }

    public Integer getQueueSize() {
        return this.queueSize;
    }

    public Integer getQueueRemainingCapacity() {
        return this.queueRemainingCapacity;
    }

    public String getQueueType() {
        return this.queueType;
    }

    public Long getKeepAliveTime() {
        return this.keepAliveTime;
    }

    public Long getCompletedTaskCount() {
        return this.completedTaskCount;
    }

    public Integer getActiveCount() {
        return this.activeCount;
    }

    public Long getTaskCount() {
        return this.taskCount;
    }

    public String getRejectHandlerClassName() {
        return this.rejectHandlerClassName;
    }

    public Boolean getAllowCoreThreadTimeOut() {
        return this.allowCoreThreadTimeOut;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setLargestPoolSize(Integer largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public void setQueueRemainingCapacity(Integer queueRemainingCapacity) {
        this.queueRemainingCapacity = queueRemainingCapacity;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setCompletedTaskCount(Long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }

    public void setRejectHandlerClassName(String rejectHandlerClassName) {
        this.rejectHandlerClassName = rejectHandlerClassName;
    }

    public void setAllowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public static class ThreadPoolExecutorDTOBuilder {
        private String poolName;
        private Integer corePoolSize;
        private Integer maximumPoolSize;
        private Integer largestPoolSize;
        private Integer poolSize;
        private Integer queueSize;
        private Integer queueRemainingCapacity;
        private String queueType;
        private Long keepAliveTime;
        private Long completedTaskCount;
        private Integer activeCount;
        private Long taskCount;
        private String rejectHandlerClassName;
        private Boolean allowCoreThreadTimeOut;

        ThreadPoolExecutorDTOBuilder() {
        }

        public ThreadPoolExecutorDTOBuilder poolName(String poolName) {
            this.poolName = poolName;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder corePoolSize(Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder maximumPoolSize(Integer maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder largestPoolSize(Integer largestPoolSize) {
            this.largestPoolSize = largestPoolSize;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder poolSize(Integer poolSize) {
            this.poolSize = poolSize;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder queueSize(Integer queueSize) {
            this.queueSize = queueSize;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder queueRemainingCapacity(Integer queueRemainingCapacity) {
            this.queueRemainingCapacity = queueRemainingCapacity;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder queueType(String queueType) {
            this.queueType = queueType;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder keepAliveTime(Long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder completedTaskCount(Long completedTaskCount) {
            this.completedTaskCount = completedTaskCount;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder activeCount(Integer activeCount) {
            this.activeCount = activeCount;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder taskCount(Long taskCount) {
            this.taskCount = taskCount;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder rejectHandlerClassName(String rejectHandlerClassName) {
            this.rejectHandlerClassName = rejectHandlerClassName;
            return this;
        }

        public ThreadPoolExecutorDTOBuilder allowCoreThreadTimeOut(Boolean allowCoreThreadTimeOut) {
            this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
            return this;
        }

        public ThreadPoolExecutorDTO build() {
            return new ThreadPoolExecutorDTO(this.poolName, this.corePoolSize, this.maximumPoolSize, this.largestPoolSize, this.poolSize, this.queueSize, this.queueRemainingCapacity, this.queueType, this.keepAliveTime, this.completedTaskCount, this.activeCount, this.taskCount, this.rejectHandlerClassName, this.allowCoreThreadTimeOut);
        }
    }
}
