package com.zy.spring.mildware.misc.threadpool;

import com.zy.spring.mildware.misc.threadpool.common.PoolName;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExceptionCatch02 {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
                2,
                0L,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new DefaultThreadFactory(PoolName.stu_executor.name()),
                new ThreadPoolExecutor.AbortPolicy());

        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        list.forEach(e -> {
            try {
                executor.execute(() -> System.out.println(e));
            } catch (Exception exception) {
                System.out.println("------>" + e);
                exception.printStackTrace();
            }
        });
        Set<Integer> set = new HashSet<>(list);
        /*set.forEach(e -> {
            try {
                executor.execute(() -> System.out.println(e));
            } catch (Exception exception) {
                System.out.println("------>" + e);
                exception.printStackTrace();
            }
        });*/
    }

}
