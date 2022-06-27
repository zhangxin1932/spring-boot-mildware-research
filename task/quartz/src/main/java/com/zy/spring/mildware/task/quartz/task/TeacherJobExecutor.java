package com.zy.spring.mildware.task.quartz.task;

import com.zy.spring.mildware.task.quartz.service.TeacherServiceImpl;

import javax.annotation.Resource;
import java.util.concurrent.atomic.LongAdder;

public class TeacherJobExecutor {

    @Resource
    private TeacherServiceImpl teacherService;

    private final LongAdder longAdder = new LongAdder();

    public void execute() {
        longAdder.increment();
        teacherService.teach(longAdder.longValue());
    }

}
