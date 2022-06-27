package com.zy.spring.mildware.task.quartz.task;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class QuartzJobBeanSupport extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobDataMap = context.getMergedJobDataMap();
            Object targetObject = jobDataMap.get("targetObject");
            String targetMethod = (String) jobDataMap.get("targetMethod");
            Method method = targetObject.getClass().getMethod(targetMethod);
            ReflectionUtils.invokeMethod(method, targetObject);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("failed to get target method for quartzJobBean.", e);
        }
    }

}
