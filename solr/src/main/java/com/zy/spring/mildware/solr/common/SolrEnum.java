package com.zy.spring.mildware.solr.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SolrEnum {

    /**
     * set
     */
    SET_FILED("set"),
    ADD_FILED("add"),

    MY_FIRST_COLLECTION("my_first_core"),

    ;

    private final String name;
}
