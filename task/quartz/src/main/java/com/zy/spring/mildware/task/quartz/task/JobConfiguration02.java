package com.zy.spring.mildware.task.quartz.task;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * https://www.cnblogs.com/FatShallot/p/12834352.html
 * https://www.cnblogs.com/eelve/p/11333897.html
 * https://www.jianshu.com/p/dc814e8014b0
 * https://www.jianshu.com/p/62fbfb6c0751
 *
 */
@Component
public class JobConfiguration02 implements ApplicationContextAware {

    private ApplicationContext applicationContext;
/*

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
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInSeconds(5) // 定时任务间隔时间
                .repeatForever(); // 触发器无限循环触发

        return TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(simpleScheduleBuilder)
                .build();
    }

    private JobDetail jobDetail() {
        // 方案1:
        */
/*QuartzJobBean quartzJobBean = new QuartzJobBean() {
            @Override
            protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
                accountService.execute();
            }
        };

        return JobBuilder.newJob(quartzJobBean.getClass())
                .storeDurably()
                .build();*//*


        // 方案2:
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("targetObject", applicationContext.getBean("accountService"));
        dataMap.put("targetMethod", "execute");
        dataMap.put("concurrent", false);

        return JobBuilder.newJob(QuartzJobBeanSupport.class)
                .usingJobData(dataMap)
                .storeDurably()
                .build();
    }
*/

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
