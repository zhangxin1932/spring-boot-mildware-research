package com.zy.spring.mildware.redis.spring.geo;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class RedisGeo {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 增加地理位置的坐标: 经纬度
     *
     * @param key
     * @param point
     * @param member
     * @return
     */
    public Long add(String key, Point point, String member) {
        return stringRedisTemplate.opsForGeo().add(key, point, member);
    }

    /**
     * 增加地理位置的坐标: 经纬度
     *
     * @param key
     * @param memberCoordinateMap
     * @return
     */
    public Long add(String key, Map<String, Point> memberCoordinateMap) {
        return stringRedisTemplate.opsForGeo().add(key, memberCoordinateMap);
    }

}
