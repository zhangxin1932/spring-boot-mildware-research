package com.zy.spring.mildware.mq.rocketmq.messagebatch;

import com.google.common.collect.Lists;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class BatchProducer {

    public static final String TOPIC = "batchTopic";
    public static final String TAGS = "batchTags";
    private static final List<Message>  list;

    static {
        Message msg1 = new Message();
        msg1.setTopic(TOPIC);
        msg1.setTags(TAGS);
        msg1.setBody("init".getBytes(StandardCharsets.UTF_8));
        Message msg2 = new Message();
        msg2.setTopic(TOPIC);
        msg2.setTags(TAGS);
        msg2.setBody("init".getBytes(StandardCharsets.UTF_8));
        Message msg3 = new Message();
        msg3.setTopic(TOPIC);
        msg3.setTags(TAGS);
        msg3.setBody("init".getBytes(StandardCharsets.UTF_8));

        list = Lists.newArrayList(msg1, msg2, msg2);
    }

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("batchProducerGroup");
        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        producer.start();

        // FIXME 这里发送批量消息时, 需要注意: 不能太大, 发送前, 要判断大小
        SendResult result = producer.send(list);
        System.out.println(result);

        producer.shutdown();
    }
}
