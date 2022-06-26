package com.zy.spring.mildware.redis.spring.controller;

import com.zy.spring.mildware.redis.spring.pubsub.RedisSubListener;
import com.zy.spring.mildware.redis.spring.util.RedisExpiredTime;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/redis/")
public class RedisMoreController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 入参
     * {
     * "k1": 1,
     * "k2": "v2"
     * }
     *
     * @param kvMap
     * @return
     */
    @RequestMapping("pipeline")
    public ResponseEntity<Object> pipeline(@RequestBody Map<String, String> kvMap) {
        if (Objects.isNull(kvMap) || kvMap.size() == 0) {
            return ResponseEntity.badRequest().body("入参非法");
        }
        List<Object> list = stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            try {
                connection.openPipeline();
                // 1.开启事务
                connection.multi();
                kvMap.forEach((k, v) -> {
                    if (Objects.nonNull(k) && Objects.nonNull(v)) {
                        // 2.编辑命令
                        connection.setEx(k.getBytes(StandardCharsets.UTF_8), RedisExpiredTime.getDefault(), v.getBytes(StandardCharsets.UTF_8));
                        // 这里模拟 命令错误, 语法正确, 但是执行错误, 发现所有仅有错误的无法执行, 其他命令正常提交
                        // 当上一步 k 对应的 v 不是数字时, 这里报错, 但不影响其他命令的提交
                        connection.incr(k.getBytes(StandardCharsets.UTF_8));
                    }
                });

                // 3.提交事务或者取消事务
                // pipeline.exec();  这里如果用 exec(), 表示提交事务, 但这里的事务不完全遵循原子性:
                // 原子性: 如果是命令错误, 假设有一条执行了 不存在的 abc 命令, 则上述几个正确的命令也都将执行失败.
                // 非原子性: 如果是运行时错误, 如上例中对 k2 进行 incr, 则仅有该命令失败, 其他正确命令都将执行成功
                connection.exec();
                // connection.discard(); // 这里如果用 discard(), 表示取消事务, 上述语句不会执行

                // 4.关闭 pipeline
                connection.closePipeline();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
        return ResponseEntity.ok(list);
    }

    @RequestMapping("msetMget")
    public ResponseEntity<Object> msetMget(@RequestBody Map<String, String> map) {
        if (Objects.isNull(map) || map.size() == 0) {
            return ResponseEntity.badRequest().body("入参非法");
        }
        stringRedisTemplate.opsForValue().multiSet(map);
        List<String> list = stringRedisTemplate.opsForValue().multiGet(map.keySet());
        return ResponseEntity.ok(Objects.requireNonNull(list));
    }

    /**
     * redis pub 消息
     * @param msg
     * @return
     */
    @RequestMapping("pub")
    public ResponseEntity<String> pub(String msg) {
        stringRedisTemplate.convertAndSend(RedisSubListener.REDIS_CHANNEL_01, msg);
        stringRedisTemplate.convertAndSend(RedisSubListener.REDIS_CHANNEL_02, msg + "__02");
        return ResponseEntity.ok("success");
    }

}
