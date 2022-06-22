package com.zy.spring.mildware.mongodb.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MongoDbCollection {

    /**
     * book
     */
    BOOK("book"),
    STUDENTS("students"),
    ITEMS("items"),


    /**************** db:file_server ***************/
    BUCKET_TIPS("bucket_tips"),

    ;

    private final String name;
}
