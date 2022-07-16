package com.zy.spring.mildware.logsxyz.logbackxyz.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class CartLog {
    public static void main(String[] args) {
        // Logger logger = LoggerFactory.getLogger("log");
        Logger logger = LogManager.getLogger(CartLog.class);
        log.info("info ................");
        logger.info("info info ................");
    }
}
