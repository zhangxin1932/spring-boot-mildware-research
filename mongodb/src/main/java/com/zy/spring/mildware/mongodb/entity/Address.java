package com.zy.spring.mildware.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "address")
@Data
public class Address {
    @Id
    private String id;
    private String provinceId;
    private List<String> cityIds;
}
