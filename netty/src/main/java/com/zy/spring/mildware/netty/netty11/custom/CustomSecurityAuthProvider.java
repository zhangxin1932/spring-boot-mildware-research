package com.zy.spring.mildware.netty.netty11.custom;

import com.zy.spring.mildware.netty.netty11.pojo.CustomSecurity;

/**
 * <p>
 * 协议安全提供接口定义
 * </p>
 */
public interface CustomSecurityAuthProvider {
    String isAllow(CustomSecurity security);
}
