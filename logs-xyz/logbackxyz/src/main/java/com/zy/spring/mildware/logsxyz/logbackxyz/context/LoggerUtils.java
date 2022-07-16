package com.zy.spring.mildware.logsxyz.logbackxyz.context;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.LevelFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.OptionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * description: 自定义的日志工具类，
 * 需要在logback.xml,logback-spring.xml或自定义的logback-custom.xml中写入基础配置
 *
 * https://blog.csdn.net/lw656697752/article/details/84904938
 * https://www.cnblogs.com/leohe/p/12117183.html
 */
public class LoggerUtils {

    private static final String CONSOLE_APPENDER_NAME = "serve-console";
    private static final String MAX_FILE_SIZE = "50MB";
    private static final String TOTAL_SIZE_CAP = "10GB";
    private static final int MAX_HISTORY = 30;
    private static ConsoleAppender defaultConsoleAppender = null;

    static {
        Map<String, Appender<ILoggingEvent>> appenderMap = allAppenders();
        appenderMap.forEach((key, appender) -> {
            // 如果logback配置文件中，已存在窗口输出的appender，则直接使用；不存在则重新生成
            if (appender instanceof ConsoleAppender) {
                defaultConsoleAppender = (ConsoleAppender) appender;
            }
        });
    }

    /**
     * description: 获取自定义的logger日志，在指定日志文件logNameEnum.getLogName()中输出日志
     * 日志中会包括所有线程及方法堆栈信息
     */
    public static <T> Logger getLogger(Class<T> clazz) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(clazz);
        LoggerContext loggerContext = logger.getLoggerContext();
        RollingFileAppender errorAppender = createAppender(clazz.getSimpleName(), Level.ERROR, loggerContext);
        RollingFileAppender infoAppender = createAppender(clazz.getSimpleName(), Level.INFO, loggerContext);
        Optional<ConsoleAppender> consoleAppender = Optional.ofNullable(defaultConsoleAppender);
        ConsoleAppender realConsoleAppender = consoleAppender.orElse(createConsoleAppender(loggerContext));
        // 设置不向上级打印信息
        logger.setAdditive(false);
        logger.addAppender(errorAppender);
        logger.addAppender(infoAppender);
        logger.addAppender(realConsoleAppender);
        return logger;
    }

    /**
     * description: 获取自定义的logger日志，在指定日志文件logNameEnum.getLogName()中输出日志
     * 日志中会包括所有线程及方法堆栈信息
     */
    public static <T> Logger getLogger(LogNameEnum logNameEnum, Class<T> clazz) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(clazz);
        LoggerContext loggerContext = logger.getLoggerContext();
        RollingFileAppender errorAppender = createAppender(logNameEnum.name(), Level.ERROR, loggerContext);
        RollingFileAppender infoAppender = createAppender(logNameEnum.name(), Level.INFO, loggerContext);
        Optional<ConsoleAppender> consoleAppender = Optional.ofNullable(defaultConsoleAppender);
        ConsoleAppender realConsoleAppender = consoleAppender.orElse(createConsoleAppender(loggerContext));
        // 设置不向上级打印信息
        logger.setAdditive(false);
        logger.addAppender(errorAppender);
        logger.addAppender(infoAppender);
        logger.addAppender(realConsoleAppender);
        return logger;
    }

    /**
     * description: 创建日志文件的file appender
     */
    private static RollingFileAppender createAppender(String name, Level level, LoggerContext loggerContext) {
        RollingFileAppender appender = new RollingFileAppender();
        // 这里设置级别过滤器
        appender.addFilter(createLevelFilter(level));
        // 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        appender.setContext(loggerContext);
        // appender的name属性
        appender.setName(name.toUpperCase() + "-" + level.levelStr.toUpperCase());
        // 读取logback配置文件中的属性值，设置文件名
        String logPath = OptionHelper.substVars("${logPath}-" + name + "-" + level.levelStr.toLowerCase() + ".log", loggerContext);
        appender.setFile(logPath);
        appender.setAppend(true);
        appender.setPrudent(false);
        // 加入下面两个节点
        appender.setRollingPolicy(createRollingPolicy(name, level, loggerContext, appender));
        appender.setEncoder(createEncoder(loggerContext));
        appender.start();
        return appender;
    }

    /**
     * description: 创建窗口输入的appender
     */
    private static ConsoleAppender createConsoleAppender(LoggerContext loggerContext) {
        ConsoleAppender appender = new ConsoleAppender();
        appender.setContext(loggerContext);
        appender.setName(CONSOLE_APPENDER_NAME);
        appender.addFilter(createLevelFilter(Level.DEBUG));
        appender.setEncoder(createEncoder(loggerContext));
        appender.start();
        return appender;
    }

    /**
     * description: 设置日志的滚动策略
     */
    private static TimeBasedRollingPolicy createRollingPolicy(String name, Level level, LoggerContext context, FileAppender appender) {
        // 读取logback配置文件中的属性值，设置文件名
        String fp = OptionHelper.substVars("${logPath}/${LOG_NAME_PREFIX}-" + name + "-" + level.levelStr.toLowerCase() + "_%d{yyyy-MM-dd}_%i.log", context);
        TimeBasedRollingPolicy rollingPolicyBase = new TimeBasedRollingPolicy<>();
        // 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        rollingPolicyBase.setContext(context);
        // 设置父节点是appender
        rollingPolicyBase.setParent(appender);
        // 设置文件名模式
        rollingPolicyBase.setFileNamePattern(fp);
        SizeAndTimeBasedFNATP sizeAndTimeBasedFNATP = new SizeAndTimeBasedFNATP();
        // 最大日志文件大小
        sizeAndTimeBasedFNATP.setMaxFileSize(FileSize.valueOf(MAX_FILE_SIZE));
        rollingPolicyBase.setTimeBasedFileNamingAndTriggeringPolicy(sizeAndTimeBasedFNATP);
        // 设置最大历史记录为30条
        rollingPolicyBase.setMaxHistory(MAX_HISTORY);
        // 总大小限制
        rollingPolicyBase.setTotalSizeCap(FileSize.valueOf(TOTAL_SIZE_CAP));
        rollingPolicyBase.start();

        return rollingPolicyBase;
    }

    /**
     * description: 设置日志的输出格式
     */
    private static PatternLayoutEncoder createEncoder(LoggerContext context) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        // 设置上下文，每个logger都关联到logger上下文，默认上下文名称为default。
        // 但可以使用<scope="context">设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
        encoder.setContext(context);
        // 设置格式
        String pattern = OptionHelper.substVars("${pattern}", context);
        encoder.setPattern(pattern);
        encoder.setCharset(Charset.forName("utf-8"));
        encoder.start();
        return encoder;
    }

    /**
     * description: 设置打印日志的级别
     */
    private static Filter createLevelFilter(Level level) {
        LevelFilter levelFilter = new LevelFilter();
        levelFilter.setLevel(level);
        levelFilter.setOnMatch(FilterReply.ACCEPT);
        levelFilter.setOnMismatch(FilterReply.DENY);
        levelFilter.start();
        return levelFilter;
    }

    /**
     * description: 读取logback配置文件中的所有appender
     */
    private static Map<String, Appender<ILoggingEvent>> allAppenders() {
        Map<String, Appender<ILoggingEvent>> appenderMap = new HashMap<>();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext(); ) {
                Appender<ILoggingEvent> appender = index.next();
                appenderMap.put(appender.getName(), appender);
            }
        }
        return appenderMap;
    }

    public enum LogNameEnum {
        /**
         * t1
         */
        t1,
        t2,
        ;
    }

}
