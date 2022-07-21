package com.zy.spring.mildware.test.spring.boot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@MapperScan("com.zy.spring.mildware.test.spring.boot.mapper")
@EnableCaching
@SpringBootApplication
public class SpringBootTestApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApp.class, args);
    }
}
