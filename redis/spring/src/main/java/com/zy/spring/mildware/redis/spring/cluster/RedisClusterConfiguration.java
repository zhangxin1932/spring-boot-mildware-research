package com.zy.spring.mildware.redis.spring.cluster;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@ConditionalOnExpression("'${spring.profiles.active}'.equals('redis-cluster')")
@Configuration
@PropertySource("classpath:application-redis-cluster.yml")
public class RedisClusterConfiguration extends CachingConfigurerSupport {

    @Value("${spring.redis.cluster.nodes}")
    private List<String> nodes;

}
