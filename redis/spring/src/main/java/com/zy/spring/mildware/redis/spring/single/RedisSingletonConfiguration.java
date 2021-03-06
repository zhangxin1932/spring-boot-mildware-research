package com.zy.spring.mildware.redis.spring.single;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;
import redis.clients.jedis.JedisPool;

@Configuration
@ConditionalOnExpression("'${spring.profiles.active}'.equals('redis-single')")
@PropertySource(value = {"classpath:application-redis-single.yml"})
public class RedisSingletonConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.lockKeyPrefix}")
    private String redisLockKeyPrefix;

    /////////////////////////////// 这一部分是 jedis 客户端 ///////////////////////////////
    @Bean
    public JedisPool jedisPool() {
        // 启动类上加 @EnableCaching
        return new JedisPool(host, port);
    }

    @Bean
    public RedisLockRegistry springRedisLockRegistry(RedisConnectionFactory connectionFactory) {
        return new RedisLockRegistry(connectionFactory, redisLockKeyPrefix);
    }
}
