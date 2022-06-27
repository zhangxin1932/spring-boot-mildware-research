package com.zy.spring.mildware.task.quartz.task;

import org.springframework.stereotype.Component;

/**
 * Scheduler（调度器）：Quartz中的任务调度器，通过Trigger和JobDetail可以用来调度、暂停和删除任务。
 * Trigger（触发器）：Quartz中的触发器，可以通过CRON表达式来指定任务执行的时间，时间到了会自动触发任务执行。
 * JobDetail（任务详情）：Quartz中需要执行的任务详情，包括了任务的唯一标识和具体要执行的任务，可以通过JobDataMap往任务中传递数据。
 * Job（任务）：Quartz中具体的任务，包含了执行任务的具体方法。
 */
@Component
public class JobConfiguration01 {
/*

    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    public SchedulerFactoryBean schedulerFactory;

    @PostConstruct
    public void init() {
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            scheduler.scheduleJob(jobDetail(), trigger());
        } catch (SchedulerException e) {
            throw new RuntimeException("failed to getScheduler.", e);
        }
    }

    private Trigger trigger() {
        return TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
    }

    private JobDetail jobDetail() {
        Job job = context -> {
            System.out.println("..................");
            accountService.execute();
            System.out.println("..................");
        };
        return JobBuilder.newJob(job.getClass())
                .usingJobData("param1", "v1")
                .storeDurably()
                .build();
    }
*/

}
