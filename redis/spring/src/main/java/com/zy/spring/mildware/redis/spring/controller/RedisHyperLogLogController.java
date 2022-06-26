package com.zy.spring.mildware.redis.spring.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.zy.spring.mildware.redis.spring.common.Constants.DATE_TIME_FORMAT_01;

/**
 * https://www.jianshu.com/p/92f6752bb8ee
 * 模糊去重计数
 * 鉴于 HyperLogLog 不保存数据内容的特性，所以，它只适用于一些特定的场景。
 * 比如:  计算日活、7日活、月活数据。
 * 微信公众号文章的阅读数，网页的 UV 统计(可利用cookie)。
 * 可以详见单元测试
 */
@RestController
@RequestMapping("hyperLogLog")
public class RedisHyperLogLogController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final String HYPER_LOG_LOG_PREFIX = "AppName_customerNo_";

    /**
     * 添加当日登录用户的会员号
     * @param customerNo
     * @return
     */
    @RequestMapping("add")
    public ResponseEntity<Long> add(String customerNo) {
        Long result = stringRedisTemplate.opsForHyperLogLog().add(String.format("%s%s", HYPER_LOG_LOG_PREFIX, customerNo), DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_01).format(LocalDateTime.now()));
        return ResponseEntity.ok(result);
    }

    /**
     * 比如: 计算事件 A 在某些 key 上的统计数字
     * @param keys
     * @return
     */
    @RequestMapping("size")
    public ResponseEntity<Long> size(String... keys) {
        Long result = stringRedisTemplate.opsForHyperLogLog().size(keys);
        return ResponseEntity.ok(result);
    }

    /**
     * 将某些 keys 的数据组合起来, 形成新的 key
     * 比如: 计算事件 A 整个 2 月份的统计数字
     * @param destinationKey
     * @param sourceKeys
     * @return
     */
    @RequestMapping("union")
    public ResponseEntity<Long> union(String destinationKey, String... sourceKeys) {
        Long result = stringRedisTemplate.opsForHyperLogLog().union(destinationKey, sourceKeys);
        return ResponseEntity.ok(result);
    }

}
