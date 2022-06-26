package com.zy.spring.mildware.mq.rocketmq.messageorder;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * 模拟一个订单的生命周期
 * 创建--付款--推送--完成
 */
public class OrderedProducer {

    private static final List<OrderInfo> orderInfos = Lists.newArrayList();
    public static final String TOPIC = "orderTopic";
    public static final String TAGS = "orderTags";

    static {
        OrderInfo o1 = new OrderInfo();
        o1.setOrderId(1L);
        o1.setStatus("创建");
        orderInfos.add(o1);
        o1 = new OrderInfo();
        o1.setOrderId(1L);
        o1.setStatus("付款");
        orderInfos.add(o1);
        o1 = new OrderInfo();
        o1.setOrderId(1L);
        o1.setStatus("推送");
        orderInfos.add(o1);
        o1 = new OrderInfo();
        o1.setOrderId(1L);
        o1.setStatus("完成");
        orderInfos.add(o1);

        OrderInfo o2 = new OrderInfo();
        o2.setOrderId(2L);
        o2.setStatus("创建");
        orderInfos.add(o2);
        o2 = new OrderInfo();
        o2.setOrderId(2L);
        o2.setStatus("付款");
        orderInfos.add(o2);

        OrderInfo o3 = new OrderInfo();
        o3.setOrderId(3L);
        o3.setStatus("创建");
        orderInfos.add(o3);
        o3 = new OrderInfo();
        o3.setOrderId(3L);
        o3.setStatus("付款");
        orderInfos.add(o3);
    }

    public static void main(String[] args) throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("orderedProducerGroup");
        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        producer.start();

        orderInfos.forEach(orderInfo -> {
            Message message = new Message();
            message.setTopic(TOPIC);
            message.setTags(TAGS);
            message.setKeys(String.valueOf(orderInfo.getOrderId()));
            message.setBody(JSON.toJSONBytes(orderInfo));
            try {
                // 这个 send 方法, 要求 MessageQueue 固定, 不能扩容或缩容
                // 配合对应的消息消费者, 共同实现顺序消息
                SendResult result = producer.send(message, new MessageQueueSelector() {
                    /**
                     * @param mqs 队列集合
                     * @param msg 消息对象
                     * @param arg 业务标识参数, 对应于 DefaultMQProducer#send(Message, MessageQueueSelector, Object) 的第三个参数
                     * @return
                     */
                    @Override
                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                        Long orderId = (Long) arg;
                        long index = orderId % mqs.size();
                        System.out.println("orderId: " + orderId + "; index: " + index);
                        return mqs.get((int) index);
                    }
                }, orderInfo.getOrderId());
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        producer.shutdown();
    }

}
