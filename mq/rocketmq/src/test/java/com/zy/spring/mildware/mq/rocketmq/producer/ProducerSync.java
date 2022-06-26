package com.zy.spring.mildware.mq.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 适用于需要等待发送结果的应用
 */
public class ProducerSync {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer();

        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");
        producer.setProducerGroup("rocketmq-zy-group");

        // 设置 topicQueue 的数量
        // producer.createTopic("rocketmq-zy-topic1");
        producer.setDefaultTopicQueueNums(20);

        producer.start();

        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setTopic("rocketmq-zy-topic1");
            message.setTags("rocketmq-zy-t1-tags1");
            message.setBody(String.format("tom:%s", i).getBytes(StandardCharsets.UTF_8));
            // 同步发送
            SendResult result = producer.send(message);
            SendStatus sendStatus = result.getSendStatus();
            String msgId = result.getMsgId();
            System.out.println(i + ".===============");
            System.out.println("result:" + result);
            System.out.println("sendStatus:" + sendStatus);
            System.out.println("msgId:" + msgId);

            TimeUnit.SECONDS.sleep(1L);
        }

        // 生产环境, 没有这句
        producer.shutdown();
    }
}
