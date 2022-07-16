package com.zy.spring.mildware.logsxyz.logbackxyz.test;

import com.zy.spring.mildware.logsxyz.logbackxyz.context.LogbackContext;

public class CardLog {

    public static void main(String[] args) {

        LogbackContext.setLogger(CardLog.class);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            /*Executors.newFixedThreadPool(5)
                    .submit(() -> {*/
                        LogbackContext.get().info("-------> " + String.valueOf(finalI));
                    /*});*/
        }

    }

}
