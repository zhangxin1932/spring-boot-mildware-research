package com.zy.spring.mildware.task.quartz.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 *
 * https://blog.csdn.net/qq_33734225/article/details/77744157
 * 注意 concurrent 属性设置为 false.
 *
 * <?xml version="1.0" encoding="GBK"?>
 * <!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
 * "http://www.springframework.org/dtd/spring-beans.dtd">
 * <beans>
 * 	<!-- 定义目标bean和bean中的方法 -->
 * 	<bean id="SpringQtzJob_SendMailJob" class="com.zy.SendMailJob" />
 * 	<bean id="SpringQtzJobMethod_SendMailJob" class="org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean">
 * 		<property name="concurrent"> <!--定时任务不会并行执行,默认是true-->
 * 			<value>false</value>
 * 		</property>
 * 		<property name="targetObject"><!--要执行的对象-->
 * 			<ref bean="SpringQtzJob_SendMailJob" />
 * 		</property>
 * 		<property name="targetMethod">  <!-- 要执行的方法名称 -->
 * 			<value>doJob</value>
 * 		</property>
 * 	</bean>
 * 	<!-- ================= 调度触发器 ======================== -->
 * 	<bean id="CronTriggerBean_SendMailJob" class="org.springframework.scheduling.quartz.CronTriggerFactoryBean">
 * 		<property name="jobDetail" ref="SpringQtzJobMethod_SendMailJob"></property>
 * 		<property name="cronExpression" value="0 0/1 * * * ?"></property><!--1分钟触发一次-->
 * 	</bean>
 * 	<!-- ======================== 调度工厂 ======================== -->
 * 	<bean id="SpringJobSchedulerFactoryBean"
 * 	class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
 * 		<property name="triggers">
 * 			<list>
 * 				<ref bean="CronTriggerBean_SendMailJob" />
 * 			</list>
 * 		</property>
 * 	</bean>
 * </beans>
 */
@Configuration
public class JobConfiguration03 {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Bean
    public TeacherJobExecutor teacherJobExecutor() {
        return new TeacherJobExecutor();
    }

    /*@Bean(value = "teacherMethodInvokingJobDetailFactoryBean")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MethodInvokingJobDetailFactoryBean teacherMethodInvokingJobDetailFactoryBean() {
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        // https://www.cnblogs.com/vic-tory/p/13289432.html
        bean.setConcurrent(false);
        bean.setTargetObject(teacherJobExecutor());
        bean.setTargetMethod("execute");
        return bean;
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CronTriggerFactoryBean teacherCronTriggerFactoryBean(@Qualifier(value = "teacherMethodInvokingJobDetailFactoryBean") MethodInvokingJobDetailFactoryBean teacherMethodInvokingJobDetailFactoryBean) {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(Objects.requireNonNull(teacherMethodInvokingJobDetailFactoryBean.getObject()));
        bean.setCronExpression("0/1 * * * * ?");
        return bean;
    }

    @PostConstruct
    public void init() {
        schedulerFactoryBean.setTriggers(teacherCronTriggerFactoryBean(teacherMethodInvokingJobDetailFactoryBean()).getObject());
    }*/

}
