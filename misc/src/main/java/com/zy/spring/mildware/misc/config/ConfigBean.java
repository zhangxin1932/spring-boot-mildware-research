package com.zy.spring.mildware.misc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;

@Configuration
public class ConfigBean {
    /*@Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new HeaderHttpSessionIdResolver("x-auth-token");
    }*/

    @Value("#{systemProperties}")
    private Map<String, Object> v;

    @Value("#{@systemProperties}")
    private Map<String, Object> v1;

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean(value = "methodInvokingFactoryBean")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setTargetObject("#{@systemProperties}");
        bean.setTargetMethod("putAll");
        bean.setArguments("");
        return bean;
    }

    @PostConstruct
    public void init() {
        System.out.println(">>>>>>>>>>");
        System.out.println(v.get("user.home"));
        System.out.println(Objects.equals(v, v1));
        System.out.println(">>>>>>>>>>");
    }

}
