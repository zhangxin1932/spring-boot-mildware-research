package com.zy.spring.mildware.task.xxl.job.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyTask {

    @XxlJob("hello1")
    public void hello1() {
        System.out.println("hello1 task.");
    }

    @XxlJob("hello2")
    public void hello2() {
        System.out.println("hello2 task.");
    }
}
