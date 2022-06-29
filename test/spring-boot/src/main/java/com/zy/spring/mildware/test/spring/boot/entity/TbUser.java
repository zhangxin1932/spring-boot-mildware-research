package com.zy.spring.mildware.test.spring.boot.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class TbUser implements Serializable {
    private static final long serialVersionUID = -1L;
    private String username;
    private String password;
}
