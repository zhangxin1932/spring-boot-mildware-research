package com.zy.spring.mildware.mongodb.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MongoTestDbEnum {
    /**
     * db1
     */
    db1("db1"),
    mri("mri"),

    ;

    private final String name;
}
