package com.zy.spring.mildware.mq.rocketmq.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 适用场景: 不关心结果, 失败了也没关系, 比如大数据中统计日志发送
 */
public class ProducerOneway {
    public static void main(String[] args) throws InterruptedException, MQClientException, RemotingException {
        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-zy-group-oneway");
        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        producer.start();

        producer.setRetryTimesWhenSendFailed(0);
        for (int i = 0; i < 5; i++) {
            Message message = new Message();
            message.setTopic("rocketmq-zy-topic3");
            message.setTags("rocketmq-zy-t1-tags3");
            message.setBody(String.format("john:%s", i).getBytes(StandardCharsets.UTF_8));
            // 发送单向消息, 不关心结果
            producer.sendOneway(message);

            TimeUnit.SECONDS.sleep(1L);
        }

        // 生产环境, 没有这句
        producer.shutdown();
    }

}
