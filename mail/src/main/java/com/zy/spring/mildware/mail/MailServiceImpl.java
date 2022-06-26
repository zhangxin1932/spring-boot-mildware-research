package com.zy.spring.mildware.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;


/**
 *
 * https://blog.csdn.net/qq_37121463/article/details/82698172
 * https://vimsky.com/examples/detail/java-method-org.springframework.mail.SimpleMailMessage.setReplyTo.html
 * https://blog.csdn.net/qq_23888451/article/details/83025948
 *
 */
@Slf4j
@Service
public class MailServiceImpl {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendSimpleMail(MailReqVO reqVO) throws Exception {
        JavaMailSenderImpl mailSender = MailUtils.MAILS_MAP.get(reqVO.getFrom());
        if (Objects.isNull(mailSender)) {
            throw new RuntimeException("cannot find the email.");
        }
        mailSender.send(buildSimpleMailMessage(reqVO));
    }

    public void sendSimpleMailWithAttachment(MailReqVO reqVO) throws Exception {
        JavaMailSenderImpl mailSender = MailUtils.MAILS_MAP.get(reqVO.getFrom());
        if (Objects.isNull(mailSender)) {
            throw new RuntimeException("cannot find the email.");
        }

        List<String> attachmentFilePaths = reqVO.getAttachmentFilePaths();
        if (CollectionUtils.isEmpty(attachmentFilePaths)) {
            mailSender.send(buildSimpleMailMessage(reqVO));
            return;
        }

        MimeMessage mimeMessage = MailUtils.getMailSender().createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
        buildSimpleMailMessage(reqVO, mimeMessageHelper);
        // 处理 attachment
        buildAttachment(mimeMessageHelper, attachmentFilePaths);
        mailSender.send(mimeMessage);
    }

    private SimpleMailMessage buildSimpleMailMessage(MailReqVO reqVO) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);

        List<String> toList = reqVO.getToList();
        if (!CollectionUtils.isEmpty(toList)) {
            String[] toArr = new String[toList.size()];
            toList.toArray(toArr);
            message.setTo(toArr);
        }

        List<String> ccList = reqVO.getCcList();
        if (!CollectionUtils.isEmpty(ccList)) {
            String[] ccArr = new String[ccList.size()];
            ccList.toArray(ccArr);
            message.setCc(ccArr);
        }

        List<String> bccList = reqVO.getBccList();
        if (!CollectionUtils.isEmpty(bccList)) {
            String[] bccArr = new String[bccList.size()];
            bccList.toArray(bccArr);
            message.setBcc(bccArr);
        }

        message.setSubject(reqVO.getSubject());
        message.setText(reqVO.getText());
        return message;
    }


    private void buildSimpleMailMessage(MailReqVO reqVO, MimeMessageHelper mimeMessageHelper) throws Exception {
        mimeMessageHelper.setFrom(from);

        List<String> toList = reqVO.getToList();
        if (!CollectionUtils.isEmpty(toList)) {
            String[] toArr = new String[toList.size()];
            toList.toArray(toArr);
            mimeMessageHelper.setTo(toArr);
        }

        List<String> ccList = reqVO.getCcList();
        if (!CollectionUtils.isEmpty(ccList)) {
            String[] ccArr = new String[ccList.size()];
            ccList.toArray(ccArr);
            mimeMessageHelper.setCc(ccArr);
        }

        List<String> bccList = reqVO.getBccList();
        if (!CollectionUtils.isEmpty(bccList)) {
            String[] bccArr = new String[bccList.size()];
            bccList.toArray(bccArr);
            mimeMessageHelper.setBcc(bccArr);
        }

        mimeMessageHelper.setSubject(reqVO.getSubject());
        mimeMessageHelper.setText(reqVO.getText());
    }

    private void buildAttachment(MimeMessageHelper mimeMessageHelper, List<String> attachmentFilePaths) throws Exception {
        if (CollectionUtils.isEmpty(attachmentFilePaths)) {
            return;
        }
        FileSystemResource resource;
        String fileName;
        // 循环处理邮件的附件
        for (String attachmentFilePath : attachmentFilePaths) {
            // 获取该路径所对应的文件资源对象
            resource = new FileSystemResource(new File(attachmentFilePath));
            // 判断该资源是否存在，当不存在时仅仅会打印一条警告日志，不会中断处理程序。
            // 也就是说在附件出现异常的情况下，邮件是可以正常发送的，所以请确定你发送的邮件附件在本机存在
            if (!resource.exists()) {
                continue;
            }
            fileName = resource.getFilename();
            mimeMessageHelper.addAttachment(fileName, resource);
        }
    }
}
