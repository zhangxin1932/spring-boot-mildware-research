package com.zy.spring.mildware.redis.redisson.single;

import com.zy.spring.mildware.redis.redisson.common.Constants;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConditionalOnExpression("'${spring.profiles.active}'.equals('redisson-single')")
@PropertySource(value = {"classpath:application-redisson-single.yml"})
public class RedissonSingleConfiguration {

    @Value("${redisson.host}")
    private String host;

    @Value("${redisson.port}")
    private String port;

    @Value("${redisson.database}")
    private String database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(String.format(Constants.ADDRESS, host, port))
                .setDatabase(Integer.parseInt(database));
        return Redisson.create(config);
    }
}
