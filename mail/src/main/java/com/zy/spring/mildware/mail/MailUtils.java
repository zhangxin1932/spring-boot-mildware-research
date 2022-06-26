package com.zy.spring.mildware.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.CollectionUtils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class MailUtils {
    private MailUtils() {
    }

    private static JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    public static Map<String, JavaMailSenderImpl> MAILS_MAP = new ConcurrentHashMap<>();
    private static final String JSON_FILE_PATH = "/opt/java_mail_senders.json";
    private static final String PROPERTIES_FILE_PATH = "/opt/java_mail_sender.properties";
    private static Properties properties;

    static {
        rebuildJavaMailSender();
        rebuildJavaMailSenders(null);
    }

    /**************************************** 只配置一个 发送邮箱的方法开始 **********************************************/
    /**
     * 读取配置文件
     */
    private static void initProperties() {
        if (properties == null) {
            try {
                // properties = PropertiesLoaderUtils.loadProperties(new PathResource(PROPERTIES_FILE_PATH));
                properties = PropertiesLoaderUtils.loadProperties(new FileUrlResource(PROPERTIES_FILE_PATH));
            } catch (Exception ex) {
                log.error("failed to load file [{}].", PROPERTIES_FILE_PATH, ex);
                properties = new Properties();
            }
        }
    }

    public static void rebuildJavaMailSender() {
        initProperties();
        mailSender.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        mailSender.setUsername(properties.getProperty("username"));
        mailSender.setPassword(properties.getProperty("password"));
        mailSender.setProtocol(properties.getProperty("protocol"));
        mailSender.setHost(properties.getProperty("host"));
    }

    public static JavaMailSenderImpl getMailSender() {
        return mailSender;
    }
    /**************************************** 只配置一个 发送邮箱的方法结束 **********************************************/


    /**************************************** 配置多个 发送邮箱的方法开始 **********************************************/
    public static Map<String, JavaMailSenderImpl> getMailSenders() {
        return MAILS_MAP;
    }

    public static void rebuildJavaMailSenders(List<MailSenderDTO> list) {
        // 这里也可以增加一步写文件的操作
       /* String json = initJsonFile();
        if (StringUtils.isEmpty(json)) {
            return;
        }
        List<MailSenderDTO> list = JSON.parseArray(json, MailSenderDTO.class);*/
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        MAILS_MAP.clear();
        list.stream().filter(Objects::nonNull).forEach(e -> {
            JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
            javaMailSender.setHost(e.getHost());
            javaMailSender.setProtocol(e.getProtocol());
            javaMailSender.setUsername(e.getUsername());
            javaMailSender.setPassword(e.getPassword());
            MAILS_MAP.put(e.getUsername(), javaMailSender);
        });
    }

    private static String initJsonFile() {
        try (FileInputStream fis = new FileInputStream(JSON_FILE_PATH);
             InputStreamReader isr = new InputStreamReader(fis);) {
            int ch;
            StringBuilder builder = new StringBuilder();
            while ((ch = isr.read()) != -1) {
                builder.append((char) ch);
            }
            return builder.toString();
        } catch (Throwable e) {
            log.error("failed to load file [{}].", JSON_FILE_PATH, e);
        }
        return "";
    }
    /**************************************** 配置多个 发送邮箱的方法结束 **********************************************/

}
