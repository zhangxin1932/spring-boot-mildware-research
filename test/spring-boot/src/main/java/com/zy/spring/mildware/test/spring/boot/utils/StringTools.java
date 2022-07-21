package com.zy.spring.mildware.test.spring.boot.utils;

import java.util.Objects;

public class StringTools {

    public static int getCharCount(String str, int c){
        if (Objects.isNull(str) || str.length() == 0){
            return -1;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i ++){
            if (str.charAt(i) == c){
                count ++;
            }
        }
        return count;
    }
}
