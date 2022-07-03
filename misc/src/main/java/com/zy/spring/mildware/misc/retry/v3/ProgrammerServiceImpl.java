package com.zy.spring.mildware.misc.retry.v3;

import com.zy.spring.mildware.misc.retry.RpcService;

public class ProgrammerServiceImpl {
    public void program(String language) {
        RpcService.getInstance().program(language);
    }
}

