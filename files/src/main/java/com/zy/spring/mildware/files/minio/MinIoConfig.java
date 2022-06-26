package com.zy.spring.mildware.files.minio;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinIoConfig {

    @Bean
    public MinioClient minioClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.0.156", 9999, true)
                .build();
        return minioClient;
    }

}
