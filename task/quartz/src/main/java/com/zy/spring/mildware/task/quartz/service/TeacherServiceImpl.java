package com.zy.spring.mildware.task.quartz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service("teacherService")
public class TeacherServiceImpl {

    @Autowired
    private ThreadPoolExecutor teacherThreadPoolExecutor;

    @Autowired
    private ThreadPoolExecutor stuThreadPoolExecutor;

    public void teach(Long times) {
//        singleton();
         multiThread(times);
    }

    // FIXME 当 MethodInvokingJobDetailFactoryBean#concurrent 为 true 时, 等价于 multiThread
    private void singleton(){
        System.out.println(Thread.currentThread().getName() + " .........> " + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()) + ": 教师开始教书.");
        // FIXME 模拟超时任务, 测试 MethodInvokingJobDetailFactoryBean#concurrent 为 false 时, 当上一个任务未执行完毕前, 是否开启下一个任务
        // @see com.zy.config.quartz.JobConfiguration03.teacherMethodInvokingJobDetailFactoryBean
        try {
            TimeUnit.SECONDS.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void multiThread(Long times) {
        for (int i = 0; i < 5; i++) {
            stuThreadPoolExecutor.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    int finalI = j;
                    teacherThreadPoolExecutor.execute(() -> costTime(times, finalI));
                }
            });
        }
    }

    private void costTime(Long times, int finalI) {
        String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        long begin = System.currentTimeMillis();
        // FIXME 模拟超时任务, 测试 MethodInvokingJobDetailFactoryBean#concurrent 为 false 时, 当上一个任务未执行完毕前, 是否开启下一个任务
        try {
            TimeUnit.SECONDS.sleep(finalI * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("%s,,,%s-第%s次开始执行, 耗时: %s.", now, Thread.currentThread().getName(), times,(end- begin)));
    }

}
