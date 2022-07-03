package com.zy.spring.mildware.misc.callback;

import lombok.Data;

@Data
public class Wrapper {
    private Object param;
    private Worker worker;
    private Listener listener;
}
