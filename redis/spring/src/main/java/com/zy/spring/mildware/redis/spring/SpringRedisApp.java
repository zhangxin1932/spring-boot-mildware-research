package com.zy.spring.mildware.redis.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringRedisApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringRedisApp.class, args);
    }
}
