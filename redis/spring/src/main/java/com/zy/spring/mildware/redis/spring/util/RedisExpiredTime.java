package com.zy.spring.mildware.redis.spring.util;

import org.springframework.util.Assert;

import java.util.concurrent.ThreadLocalRandom;

public class RedisExpiredTime {

    private static final long DEFAULT_EXPIRED_TIME = 1800L;
    private static final long DEFAULT_RANDOM_TIME = 300L;

    /**
     * 获取缓存时间, 单位: s
     *
     * @param standardExpiredTime 标准过期时间
     * @param randomTime              在一定范围内随机的过期时间
     * @return 过期时间, 防止缓存穿透
     */
    public static long get(long standardExpiredTime, long randomTime) {
        Assert.isTrue(standardExpiredTime - randomTime > 0, String.format("standardExpiredTime: %s must larger than random:%s, please check.", standardExpiredTime, randomTime));
        ThreadLocalRandom current = ThreadLocalRandom.current();
        return current.nextLong(standardExpiredTime - randomTime, standardExpiredTime + randomTime);
    }

    public static long getDefault() {
        return get(DEFAULT_EXPIRED_TIME, DEFAULT_RANDOM_TIME);
    }

}
