package com.zy.spring.mildware.mail;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailSenderDTO implements Serializable {
    private static final long serialVersionUID = 812088345428675770L;
    /**
     * 发件人账号
     */
    private String username;
    /**
     * 发件人密码
     */
    private String password;
    /**
     * Host
     */
    private String host;
    /**
     * 协议类型
     */
    private String protocol;
}
