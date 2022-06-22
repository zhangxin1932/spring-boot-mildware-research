package com.zy.spring.mildware.mongodb.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Items {
    private String id;
    private String item;
    private double price;
    private double quantity;
    private Date date;
}
