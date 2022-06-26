package com.zy.spring.mildware.redis.redisson.controller;

import com.zy.spring.mildware.redis.redisson.common.Constants;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redissonLock/")
public class RedissonLockController {

    @Resource
    private RedissonClient client;

    private static final RedissonClient CLIENT1;
    private static final RedissonClient CLIENT2;
    private static final RedissonClient CLIENT3;
    private static final RedissonClient CLIENT4;
    private static final RedissonClient CLIENT5;

    private int count = 20;

    static {
        Config config1 = new Config();
        config1.useSingleServer().setAddress("redis//192.168.0.156:6379");
        CLIENT1 = Redisson.create(config1);

        Config config2 = new Config();
        config2.useSingleServer().setAddress("redis//192.168.0.156:6380");
        CLIENT2 = Redisson.create(config2);

        Config config3 = new Config();
        config3.useSingleServer().setAddress("redis//192.168.0.156:6381");
        CLIENT3 = Redisson.create(config3);

        Config config4 = new Config();
        config4.useSingleServer().setAddress("redis//192.168.0.156:6382");
        CLIENT4 = Redisson.create(config4);

        Config config5 = new Config();
        config5.useSingleServer().setAddress("redis//192.168.0.156:6383");
        CLIENT5 = Redisson.create(config5);
    }

    /**
     *
     * http://redis.cn/topics/distlock
     * redis 官网的红锁算法实现方式, 要求:
     * 假设有 N(这里N是5) 个 Redis master。这些节点完全互相独立，不存在主从复制或者其他集群协调机制。
     * 也即: 这 5 个节点之间, 不构成主从/哨兵/集群, 互相之间不通信.
     * @param goodsId
     * @return
     */
    @RequestMapping("redLock")
    public ResponseEntity<Object> redLock(String goodsId) {
        RLock lock1 = CLIENT1.getLock(goodsId);
        RLock lock2 = CLIENT2.getLock(goodsId);
        RLock lock3 = CLIENT3.getLock(goodsId);
        RLock lock4 = CLIENT4.getLock(goodsId);
        RLock lock5 = CLIENT5.getLock(goodsId);

        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3, lock4, lock5);
        boolean lock = redLock.tryLock();
        if (lock) {
            try {
                System.out.println("获取锁成功, 执行任务, goodsId: " + goodsId);
                TimeUnit.SECONDS.sleep(1L);
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                redLock.unlock();
            }
        }

        return ResponseEntity.ok(lock);
    }

    @RequestMapping("secKill")
    public ResponseEntity<Object> secKill(String goodsId) {
        RLock lock = client.getLock(String.format(Constants.LOCK_PREFIX, goodsId));
        boolean b = lock.tryLock();
        String msg = "扣减失败";
        if (b) {
            try {
                int count = getCount();
                if (count > 0) {
                    decreaseCount();
                    msg = "扣减成功";
                } else {
                    msg = "库存不足";
                }
            } finally {
                lock.unlock();
            }
        }
        return ResponseEntity.ok(msg);
    }

    /**
     * 模拟从 DB 中查询出来的库存数据
     */
    private int getCount() {
        return count;
    }

    private void decreaseCount() {
        this.count--;
    }
}
