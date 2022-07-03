package com.zy.spring.mildware.misc.retry.v2;

import com.zy.spring.mildware.misc.retry.RpcService;

public class TeacherServiceImplRetryV2 implements ITeacherService {
    @Override
    public void teach(String subjectName) {
        RpcService.getInstance().teach(subjectName);
    }
}
