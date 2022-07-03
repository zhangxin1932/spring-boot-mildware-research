package com.zy.spring.mildware.redis.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Objects;

@SpringBootTest
//@RunWith(SpringRunner.class)
public class RedisSinglePubSubTest {
    @Autowired
    private JedisPool jedisPool;

    @Test
    public void fn01() {
        Jedis jedis = jedisPool.getResource();
        if (Objects.nonNull(jedis)) {
            try {
                jedis.publish("topic01", "hello-world");
            } finally {
                jedis.close();
            }
        }
    }
}
