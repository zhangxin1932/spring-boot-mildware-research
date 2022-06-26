package com.zy.spring.mildware.redis.redisson.controller;

import com.google.common.collect.Lists;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/redissonLua/")
public class RedissonLuaController {

    @Resource
    private RedissonClient client;

    /**
     * FIXME 这里定义了 StringCodec 类型的编解码器, 是因为其默认的编解码器是: MarshallingCodec
     * FIXME 而当使用 lua 脚本时, 要调用 lua 的 tonumber 函数 将库存(string类型) 转为 number 类型时,
     * FIXME 如果用默认的编解码器, 将会得到 nil 的结果, 会出错.
     * FIXME 故这里使用了 StringCodec 来解决, 也可以用 IntegerCodec 或 LongCodec.
     */
    private static final Codec CODEC = StringCodec.INSTANCE;

    /**
     * 将数据全量加载至 redis 中
     */
    private static final String SETNX_LUA_SCRIPT = "return redis.call('setnx',KEYS[1],ARGV[1]);";

    /**
     * 查询 redis 中某商品库存
     */
    private static final String GET_LUA_SCRIPT = "return redis.call('get', KEYS[1]);";

    /**
     * 扣减某商品库存的 lua 脚本
     */
    private static final String DECREASE_GOODS_COUNT_LUA_SCRIPT =
            "if (redis.call('exists', KEYS[1]) == 0) then " +
                    "return 0; " +
                    "end;" +
                    "local count = redis.call('get', KEYS[1]); " +
                    "local decrementCount = ARGV[1]; " +
                    "local a = tonumber(count); " +
                    "local b = tonumber(decrementCount); " +
                    "if (a < b) then " +
                    "return 0; " +
                    "end; " +
                    "redis.call('set', KEYS[1], (a - b)); " +
                    "return 1; ";

    @RequestMapping("addGoods")
    public ResponseEntity<Object> addGoods(String goodsId, Integer goodsCount) {
        Object result = client.getScript(CODEC).eval(RScript.Mode.READ_WRITE, SETNX_LUA_SCRIPT, RScript.ReturnType.INTEGER, Lists.newArrayList(goodsId), goodsCount);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("getGoodsCount")
    public ResponseEntity<Object> getGoodsCount(String goodsId) {
        Object result = client.getScript(CODEC).eval(RScript.Mode.READ_ONLY, GET_LUA_SCRIPT, RScript.ReturnType.VALUE, Lists.newArrayList(goodsId));
        return ResponseEntity.ok(result);
    }

    @RequestMapping("decreaseGoodsCount")
    public ResponseEntity<Object> decreaseGoodsCount(String goodsId, Integer decreaseCount) {
        Object result = client.getScript(CODEC).eval(RScript.Mode.READ_WRITE, DECREASE_GOODS_COUNT_LUA_SCRIPT, RScript.ReturnType.INTEGER, Lists.newArrayList(goodsId), decreaseCount);
        return ResponseEntity.ok(result);
    }
}
