package com.zy.spring.mildware.mail;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class MailSenderDTO implements Serializable {
    private static final long serialVersionUID = 812088345428675770L;
    /**
     * 发件人账号
     */
    @NotEmpty(message = "username 不能为空")
    private String username;
    /**
     * 发件人密码
     */
    @NotEmpty(message = "password 不能为空")
    private String password;
    /**
     * Host
     */
    @NotEmpty(message = "host 不能为空")
    private String host;
    /**
     * 协议类型
     */
    @NotEmpty(message = "protocol 不能为空")
    private String protocol;
}
