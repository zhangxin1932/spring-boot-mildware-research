package com.zy.spring.mildware.netty.common;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public interface Constants {
    String CUSTOM_DELIMITER_01 = "$_";
    Integer CONNECT_TIMEOUT_MILLS = 3000;
    Integer RECONNECT_TIMEOUT_MILLS = 180;
    int MAGIC_NUMBER = 0x1314;
    byte MAIN_VERSION = 1;
    byte SUB_VERSION = 1;
    byte MODIFY_VERSION = 1;
    int SESSION_ID_LENGTH = 8;

    static byte[] paddingBlankBytes(String str, int length) {
        if (Objects.isNull(str)) {
            return null;
        }
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] dest = new byte[length];
        System.arraycopy(bytes, 0, dest, 0, bytes.length);
        return dest;
    }

}
