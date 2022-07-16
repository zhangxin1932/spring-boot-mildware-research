package com.zy.spring.mildware.logs.logback.context;

import org.slf4j.Logger;

public class LogbackContext {

    private static final ThreadLocal<Logger> LOGGER_THREAD_LOCAL = new ThreadLocal<>();

    public static Logger get() {
        return LOGGER_THREAD_LOCAL.get();
    }

    public static <T> void setLogger(Class<T> clazz) {
        LOGGER_THREAD_LOCAL.set(LoggerUtils.getLogger(clazz));
    }

    public static void remove() {
        LOGGER_THREAD_LOCAL.remove();
    }

}
