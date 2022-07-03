package com.zy.spring.mildware.redis.redisson.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.zy.spring.mildware.redis.redisson.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DelayedOrderDTO implements Serializable {
    private static final long serialVersionUID = -7363325932234810555L;
    private String orderId;
    private String orderName;
    private String payAmount;
    /**
     *
     */
    private int orderStatus;
    @JSONField(format = Constants.TIME_PATTERN_01)
    private LocalDateTime createTime;
    @JSONField(format = Constants.TIME_PATTERN_01)
    private LocalDateTime updateTime;

    public String getCreateTime() {
        return Objects.isNull(createTime) ? null : DateTimeFormatter.ofPattern(Constants.TIME_PATTERN_01).format(createTime);
    }

    public String getUpdateTime() {
        return Objects.isNull(updateTime) ? null : DateTimeFormatter.ofPattern(Constants.TIME_PATTERN_01).format(createTime);
    }
}
