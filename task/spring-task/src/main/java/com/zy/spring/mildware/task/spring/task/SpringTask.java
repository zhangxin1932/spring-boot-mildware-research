package com.zy.spring.mildware.task.spring.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@EnableScheduling
public class SpringTask {

    // @Scheduled(cron = "0/5 * * * * ?")  // 每5秒执行一次
    @Scheduled(fixedDelay = 2000)  // 每2秒执行一次
    public void saySchedule(){
        AtomicLong num = new AtomicLong();
        System.out.println(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").format(LocalDateTime.now()) + " ---> " + num.incrementAndGet());
    }

}
