package com.zy.spring.mildware.mq.rocketmq.messagetransaction;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * RocketMQ 采用两阶段提交 的方式实现事务消息， TransactionMQProducer
 * 处理上面情况的流程是，先发一个“准备从 B 银行账户增加一万元”的消息，
 * 发送成功后做从 A 银行账户扣除一万元的操作 ，根据操作结果是否成功，确定
 * 之前的“准备从 B 银行账户增加一万元”的消息是做 commit 还是 rollback ，具
 * 体流程如下：
 * 1 ）发送方向 RocketMQ 发送“待确认”消息 。
 * 2) RocketMQ 将收到的“待确认” 消息持久化成功后， 向发送方回复消息
 * 已经发送成功，此时第一阶段消息发送完成 。
 * 3 ）发送方开始执行本地事件逻辑 。
 * 4 ）发送方根据本地事件执行结果向 RocketMQ 发送二次确认（ Commit 或
 * 是 Rollback ） 消息 ， RocketMQ 收到 Commit 状态则将第一阶段消息标记为可投
 * 递，订阅方将能够收到该消息；收到 Rollback 状态则删除第一阶段的消息，订
 * 阅方接收不到该消息 。
 * 5 ）如果出现异常情况，步骤 4 ）提交的二次确认最终未到达 RocketMQ,
 * 服务器在经过固定时间段后将对“待确认”消息、发起回查请求 。
 * 6 ）发送方收到消息回查请求后（如果发送一阶段消息的 Producer 不能工
 * 作，回查请求将被发送到和 Producer 在同一个 Group 里的其他 Producer ），通
 * 过检查对应消息 的本地事件执行结果返回 Commit 或 Roolback 状态 。
 * 7) RocketMQ 收到回查请求后，按照步骤 4 ） 的逻辑处理 。
 * 上面的逻辑似乎很好地实现了事务消息功能 ，它也是 RocketMQ 之前的版
 * 本实现事务消息 的逻辑 。 但是因为 RocketMQ 依赖将数据顺序写到磁盘这个
 * 特征来提高性能，步骤 4 ）却需要更改第一阶段消息的状态，这样会造成磁盘
 * Catch 的脏页过多， 降低系统的性能 。 所以 RocketMQ 在 4.x 的版本中将这部分
 * 功能去除 。 系统中的一些上层 Class 都还在，用户可以根据实际需求实现自己
 * 的事务功能 。
 */
public class TransactionProducer {
    public static final String TOPIC = "transactionTopic";
    public static final String TAGS = "transactionTags";

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        TransactionMQProducer producer = new TransactionMQProducer("transactionProducerGroup");
        producer.setNamesrvAddr("192.168.0.156:9876;192.168.0.156:9877");

        producer.start();

        // 未 commit 的 msg, consumer 是不可见的.
        producer.setTransactionListener(new TransactionListener() {

            /**
             * 在该方法中执行本地事务
             * @param msg
             * @param arg 这个 args 对应于 TransactionMQProducer#sendMessageInTransaction(final Message msg,  Object arg) 中的 arg
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                int i = (int) arg;
                if (i == 0 || i == 1) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else if (i == 2 || i == 3){
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                return LocalTransactionState.UNKNOW;
            }

            /**
             * 消息补偿机制:
             * 当本地提交了 msg, 但超过一定时间后始终没向 broker 发送 commit 或 rollback,
             * broker 会回调该方法, 该方法中执行完逻辑后, 最终向 broker 发送到底是 commit 还是 rollback 对应的 msg.
             * @param msg
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // 这里根据本地事务情况, 进行 rollback 或者 commit.
                System.out.println("checkLocalTransaction: " + new String(msg.getBody(), StandardCharsets.UTF_8));
                return LocalTransactionState.COMMIT_MESSAGE;
            }
        });

        for (int i = 0; i < 5; i++) {
            Message message = new Message(TOPIC, TAGS, ("hello-world --> " + i).getBytes(StandardCharsets.UTF_8));
            SendResult result = producer.sendMessageInTransaction(message, i);
            System.out.println(result);
        }

        // 这里要回查, 不要停止了
        // producer.shutdown();

    }
}
