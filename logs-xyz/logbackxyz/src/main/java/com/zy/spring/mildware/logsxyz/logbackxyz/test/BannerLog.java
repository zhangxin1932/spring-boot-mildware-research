package com.zy.spring.mildware.logsxyz.logbackxyz.test;

import com.zy.spring.mildware.logsxyz.logbackxyz.context.LogbackContext;

public class BannerLog {

    public static void main(String[] args) {
        LogbackContext.setLogger(BannerLog.class);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            /*Executors.newFixedThreadPool(5)
                    .submit(() -> {*/
                        LogbackContext.get().info("-------> " + String.valueOf(finalI));
                    /*});*/
        }
    }

}
