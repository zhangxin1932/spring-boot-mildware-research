package com.zy.spring.mildware.rdbms.jpa.dsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// @EnableJpaRepositories // 这个注解可以不加
@EntityScan(basePackages = "com.zy.*.entity") // 这里由于 entity 在公共包里, 这里需要自行扫描了
@EnableTransactionManagement
@SpringBootApplication
public class JpaDslApp {
    public static void main(String[] args) {
        SpringApplication.run(JpaDslApp.class, args);
    }
}
