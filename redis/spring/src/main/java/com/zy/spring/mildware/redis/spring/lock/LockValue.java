package com.zy.spring.mildware.redis.spring.lock;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LockValue {

    public static final String DATE_FORMAT_01 = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * UUID 即可
     */
    private String randomValue;
    /**
     * yyyy-MM-dd HH::mm:ss.SSS
     */
    private String startTime;

    public static String buildDefaultRandomValue(String datetimeFormat) {
        LockValue lockValue = new LockValue(UUID.randomUUID().toString(), DateTimeFormatter.ofPattern(datetimeFormat).format(LocalDateTime.now()));
        return JSON.toJSONString(lockValue);
    }

    public static String buildDefaultRandomValue() {
        return buildDefaultRandomValue(DATE_FORMAT_01);
    }

    public static LockValue parseDefaultRandomValue(String randomValue) {
        return JSON.parseObject(randomValue, LockValue.class);
    }

}
