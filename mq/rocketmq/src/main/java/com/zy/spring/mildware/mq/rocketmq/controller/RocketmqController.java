package com.zy.spring.mildware.mq.rocketmq.controller;

import com.zy.spring.mildware.mq.rocketmq.producer.RocketmqProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rocketmq/")
public class RocketmqController {

    @Resource
    private RocketmqProducer rocketmqProducer;

    @RequestMapping("send")
    public ResponseEntity<String> send(String message) {
        try {
            rocketmqProducer.send(message);
            return ResponseEntity.ok().body("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failure");
    }

}
