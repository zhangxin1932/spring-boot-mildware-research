package com.zy.spring.mildware.rdbms.jpa.dsl.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 绩效枚举
 */
@AllArgsConstructor
@Getter
public enum PerformanceDegree {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    ;
    private String degree;
}
