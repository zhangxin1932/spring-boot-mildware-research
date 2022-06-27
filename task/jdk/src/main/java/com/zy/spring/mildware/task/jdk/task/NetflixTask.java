package com.zy.spring.mildware.task.jdk.task;

import com.netflix.config.DynamicProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 默认的，Archaius将查找classpath下名为config.properties文件并读取，这个配置文件可以使包含在一个jar包的根路径下。
 * 另外，你可以使用属性archaius.configurationSource.additionalUrls来包含url形式的文件，多个文件用逗号分割。
 *   注意：配置多属性文件时的属性覆盖，最后读到的属性会覆盖前面相同的属性
 * 列出我们可以修改的一些系统属性
 * Operation 	                                            HTTP action 	                                                            Notes
 * archaius.configurationSource.defaultFileName 	            指定Archaius默认加载的配置源属性文件名，默认：classpath:config.properties 	 config.properties
 * archaius.fixedDelayPollingScheduler.initialDelayMills 	延迟加载，默认30秒 	                                                        30000
 * archaius.fixedDelayPollingScheduler.delayMills 	        两次属性读取时间间隔，默认1分钟 	                                            60000
 */
@Component
@Slf4j
public class NetflixTask {
    private static final ScheduledExecutorService executorService = Executors
            .newSingleThreadScheduledExecutor();

    private Long fixedDelay = DynamicProperty.getInstance("fixedDelay").getLong();

    private void execute() {
        try {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>try块里写循环定时执行的逻辑<<<<<<<<<<<<<<<<<<");
            // 每次业务执行完后,重新由netflix动态获取执行时间,列入下次定时任务中
            fixedDelay = DynamicProperty.getInstance("fixedDelay").getLong();
        } catch (Exception e) {
            log.error("failed to execute task", e);
        } finally {
            // 无论如何,都会执行该方法
            executorService.schedule(this::execute, fixedDelay, TimeUnit.SECONDS);
        }
    }

    @PostConstruct
    public void initMethod(){
        // 定时任务的触发点,该bean初始化后,执行该方法
        execute();
    }

}
