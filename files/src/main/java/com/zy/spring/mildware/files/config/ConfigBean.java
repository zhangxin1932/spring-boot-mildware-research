package com.zy.spring.mildware.files.config;

import com.zy.spring.mildware.files.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {

    @Bean
    CommandLineRunner init(final StorageService storageService) {
        return args -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
