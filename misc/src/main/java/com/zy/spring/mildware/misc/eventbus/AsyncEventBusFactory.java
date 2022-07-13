package com.zy.spring.mildware.misc.eventbus;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncEventBusFactory {

    private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();
    private static final AsyncEventBus ASYNC_EVENT_BUS = new AsyncEventBus(EXECUTOR);

    public static void register(Object listener) {
        ASYNC_EVENT_BUS.register(listener);
    }

    public static void post(Object event) {
        ASYNC_EVENT_BUS.post(event);
    }

    public static void unregister(Object listener) {
        ASYNC_EVENT_BUS.unregister(listener);
    }
}
