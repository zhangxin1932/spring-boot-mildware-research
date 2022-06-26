package com.zy.spring.mildware.redis.spring.lock;

import com.google.common.collect.Lists;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Objects;

/**
 * http://redis.cn/topics/distlock.html
 * https://redis.io/topics/distlock
 */
@Component
public class RedisLock {

    public static final Long REDIS_LOCK_OR_UNLOCK_SUCCESS = 1L;
    public static final Long REDIS_DEAD_LOCK_EXPIRED_TIME = -1L;
    public static final String OK = "OK";

    private RedisScript<String> redisLockScript;
    private RedisScript<Long> redisUnlockScript;

    @PostConstruct
    public void init() {
        String lockScript = "return redis.call('set',KEYS[1],ARGV[1],'NX','EX',ARGV[2]);";
        redisLockScript = new DefaultRedisScript<>(lockScript, String.class);

        String unlockScript = "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                "return redis.call('del',KEYS[1]) " +
                "else " +
                "return 0 " +
                "end;";
        redisUnlockScript = new DefaultRedisScript<>(unlockScript, Long.class);
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 该方法只尝试一次, 失败即返回, 若需要循环获取锁, 可另行封装或参考 spring-integration-redis
     *
     * 由于 StringRedisTemplate 的 key 和 values, args 的默认序列化方式是 StringRedisSerializer, 所以参数均为 String
     * 单机模式下, 锁在单一节点上
     * 主从 & 哨兵模式下, 锁在 master 上, slave 从 master 同步节点
     * 集群模式下, key 会被 hash 到 16384 个 slots 的其中一个的 slot 上 (该 slot 只会存在一个 master 节点及该 master 的 slave节点上)
     *
     * 个人理解: redis 官网中提到的半数以上节点加锁成功, 才算获取到的锁的前提是:
     * 我们假设有 N 个 Redis master。这些节点完全互相独立，不存在主从复制或者其他集群协调机制.
     * 而常规情况下搭建的 redis 集群, 是存在集群协调机制的.
     * 也即要满足官网算法, 需搭建几个完全不通信的 redis 节点, 专门用于分布式锁服务.
     * @param key 键
     * @param randomValue 随机值
     * @param expireTime 过期时间, 这里单位是 s, 因为 lua 脚本中参数是 EX; 如果是 PX, 则为 ms.
     * @return true, 加锁成功; false, 加锁失败
     */
    public boolean lock(String key, String randomValue, String expireTime) {
        String result = stringRedisTemplate.execute(redisLockScript, Lists.newArrayList(key), randomValue, expireTime);
        if (Objects.isNull(result)) {
            return false;
        }
        if (Objects.equals(result.toUpperCase(), OK)) {
            return true;
        }
        // 补偿机制
        Long ttlTime = stringRedisTemplate.getExpire(key);
        if (Objects.equals(REDIS_DEAD_LOCK_EXPIRED_TIME, ttlTime)) {
            // FIXME 打个日志, 标记删除了死锁
            stringRedisTemplate.delete(key);
        }
        return false;
    }

    /**
     * 释放锁前, 要校验 value. 以防删掉其他人的锁, 比如:
     * t1 对 k1 加锁并且成功了, 然后执行任务被阻塞, 超时后, 锁释放了;
     * t2 再对 k1 加锁并且成功了, 正在执行任务, 但此时 t1 任务执行完毕, 没有校验 value, 直接删除了锁, 那么就会造成问题
     *
     * @param key 键
     * @param randomValue 随机值
     * @return true, 锁释放成功; false, 锁释放失败
     */
    public boolean unlock(String key, String randomValue) {
        return Objects.equals(stringRedisTemplate.execute(redisUnlockScript, Lists.newArrayList(key), randomValue), REDIS_LOCK_OR_UNLOCK_SUCCESS);
    }

}
