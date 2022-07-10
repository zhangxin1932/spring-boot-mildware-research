package com.zy.spring.mildware.netty.netty11.support;


import com.zy.spring.mildware.netty.netty11.custom.CustomSecurityAuthProvider;
import com.zy.spring.mildware.netty.netty11.pojo.CustomSecurity;

/**
 * <p>
 * 协议安全认证默认实现
 * </p>
 */
public class DefaultCustomSecurityAuthProvider implements CustomSecurityAuthProvider {

    @Override
    public String isAllow(CustomSecurity security) {
        return null;
    }
}
