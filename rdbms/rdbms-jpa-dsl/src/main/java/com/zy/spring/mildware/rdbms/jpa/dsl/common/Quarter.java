package com.zy.spring.mildware.rdbms.jpa.dsl.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 季度枚举
 */
@Getter
@AllArgsConstructor
public enum Quarter {
    Q1("Q1"),
    Q2("Q2"),
    Q3("Q3"),
    Q4("Q4"),
    Q("Q")
    ;
    private String name;
}
