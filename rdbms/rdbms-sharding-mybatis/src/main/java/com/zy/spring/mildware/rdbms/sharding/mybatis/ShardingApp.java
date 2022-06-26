package com.zy.spring.mildware.rdbms.sharding.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.zy.spring.mildware.rdbms.sharding.mybatis.mapper")
@SpringBootApplication
public class ShardingApp {
    public static void main(String[] args) {
        SpringApplication.run(ShardingApp.class, args);
    }
}
