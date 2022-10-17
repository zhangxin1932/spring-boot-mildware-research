package com.zy.spring.mildware.solr.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class MriEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String apiId;
    private String productKey;
    private String name;
    private String domainKey;
    private String outcome;
    private String dateOfOutcome;
    private String maHolder;
    private String publishAt;
    private String updatedAt;
}
