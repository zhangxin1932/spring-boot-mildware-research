package com.zy.spring.mildware.redis.spring.controller;

import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.redis.spring.lock.LockValue;
import com.zy.spring.mildware.redis.spring.lock.RedisLock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RestController
@RequestMapping("/lock/")
public class RedisLockController {

    private static final String KEY_PREFIX = "my:lock:%s";
    private static final String EXPIRED_TIME = "180";

    @Resource
    private RedisLock redisLock;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisLockRegistry springRedisLockRegistry;

    /**
     * spring 官方提供的分布式锁使用示例, 这里会有一个锁重试的时间, 而不是首次获取锁失败后立即放弃
     */
    @RequestMapping("lock")
    public void orderLock() {
        Lock lock = springRedisLockRegistry.obtain("my:key");
        boolean b = lock.tryLock();
        if (!b) {
            System.out.println("获取锁失败..");
            return;
        }
        try {
            System.out.println("获取锁成功, 执行业务逻辑");
            TimeUnit.MILLISECONDS.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 业务中使用 redis 分布式锁示例
     *
     * @param key
     * @return
     */
    @RequestMapping("biz")
    public boolean biz(String key) {
        String randomValue = LockValue.buildDefaultRandomValue();
        boolean locked = redisLock.lock(key, randomValue, EXPIRED_TIME);
        if (!locked) {
            System.out.println("没有获取到锁");
            return false;
        }
        try {
            System.out.println("获取到锁, 执行任务");
            TimeUnit.SECONDS.sleep(1L);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            redisLock.unlock(key, randomValue);
        }
        return true;
    }

    /**
     * 根据 key 获取到存储到 redis 中的 value, 进而拿到 startTime
     * startTime 可用于监控锁存储到 redis 中的时间, 如果过长, 提示可能发生死锁
     *
     * @param key
     * @return
     */
    @RequestMapping("getValueStartTime")
    public String getValueStartTime(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (Objects.isNull(value)) {
            return null;
        }
        LockValue lockValue = JSON.parseObject(value, LockValue.class);
        if (Objects.isNull(lockValue)) {
            return null;
        }
        return lockValue.getStartTime();
    }

}
