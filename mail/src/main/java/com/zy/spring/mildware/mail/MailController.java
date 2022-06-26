package com.zy.spring.mildware.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/mail/")
@Slf4j
@Validated
public class MailController {

    @Autowired
    private MailServiceImpl mailService;

    @RequestMapping("sendSimpleMail")
    public ResponseEntity<String> sendSimpleMail(@RequestBody MailReqVO reqVO) {
        try {
            mailService.sendSimpleMail(reqVO);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            log.error("failed to sendSimpleMail.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 基本流程:
     *  1.填写邮件主题, 收件人
     *  2.上传附件 --> 单独提供了一个文件上传接口, 上传后对应于 attachmentFilePaths
     *  (上传到服务器, 这里未设置文件服务器, 故要求上传的文件不能太大, 太多, 限制下文件大小和文件数量
     *  3.开始发送邮件
     * @param reqVO
     * @return
     */
    @RequestMapping("sendSimpleMailWithAttachment")
    public ResponseEntity<String> sendSimpleMailWithAttachment(@RequestBody MailReqVO reqVO) {
        try {
            mailService.sendSimpleMailWithAttachment(reqVO);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            log.error("failed to sendSimpleMailWithAttachment.", e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failure");
    }

    @RequestMapping("uploadMails")
    public ResponseEntity<String> uploadMails(@RequestBody List<MailSenderDTO> list) {
        try {
            MailUtils.rebuildJavaMailSenders(list);
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            log.error("failed to uploadMailProperties.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failure");
        }
    }

}
