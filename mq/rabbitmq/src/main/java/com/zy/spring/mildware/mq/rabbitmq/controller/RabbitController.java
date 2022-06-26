package com.zy.spring.mildware.mq.rabbitmq.controller;

import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.mq.rabbitmq.common.RabbitConstants;
import com.zy.spring.mildware.mq.rabbitmq.entity.RabbitBean;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("rabbitmq")
// https://blog.csdn.net/aa1215018028/article/details/81325082
public class RabbitController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 这里 模拟 producer
     *
     * @param rabbitBean
     * @return
     */
    @RequestMapping("delayQueue")
    public String delayQueue(@RequestBody RabbitBean rabbitBean) {
        try {
            rabbitTemplate.convertAndSend(RabbitConstants.DELAY_EXCHANGE, RabbitConstants.DELAY_ROUTING_KEY, JSON.toJSONString(rabbitBean));
            return "success";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "failure";
        }
    }

    /**
     * 这里 模拟 producer
     *
     * @param rabbitBean
     * @return
     */
    @RequestMapping("delayQueueByConfirm")
    public String delayQueueByConfirm(@RequestBody RabbitBean rabbitBean) {
        try {
            Map<String, Object> properties = new HashMap<>();
            properties.put("age", rabbitBean.getAge());
            properties.put("name", rabbitBean.getName());
            send("Hello RabbitMQ For Spring Boot!", properties);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }

    // 回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("correlationData: " + correlationData);
            System.err.println("ack: " + ack);
            if(!ack){
                System.err.println("异常处理....");
            }
        }
    };

    // 回调函数: return返回
    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
                                    String exchange, String routingKey) {
            System.err.println("return exchange: " + exchange + ", routingKey: "
                    + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
        }
    };

    // 发送消息方法调用: 构建Message消息
    private void send(Object message, Map<String, Object> properties) throws Exception {
        MessageHeaders mhs = new MessageHeaders(properties);
        Message msg = MessageBuilder.createMessage(message, mhs);
        rabbitTemplate.convertSendAndReceive(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        //id + 时间戳 全局唯一 
        CorrelationData correlationData = new CorrelationData("1234567890");
        rabbitTemplate.convertAndSend(RabbitConstants.DELAY_EXCHANGE, RabbitConstants.DELAY_ROUTING_KEY, msg, correlationData);
    }

}
