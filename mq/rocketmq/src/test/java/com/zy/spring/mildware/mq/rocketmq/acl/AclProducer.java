package com.zy.spring.mildware.mq.rocketmq.acl;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

/**
 * 1.修改 tools.sh 配置文件
 * https://blog.csdn.net/qq_42184068/article/details/105910181
 *
 * 2.配置权限 (画个流程图)
 * sh mqadmin updateAclConfig -n 192.168.0.156:9876 -b 192.168.0.156:10911 -a rockettomcat -s 123456789 -t aclTopic=PUB|SUB -g rocketmq-zy-group-consumer=SUB
 * https://blog.csdn.net/prestigeding/article/details/94317946
 *
 * - accessKey: rockettomcat
 *   secretKey: '123456789'
 *   admin: false
 *   topicPerms:
 *   - aclTopic=PUB|SUB
 *   groupPerms:
 *   - rocketmq-zy-group-consumer=SUB
 *
 * 3.ACL权限校验类
 * org.apache.rocketmq.acl.plain.PlainPermissionManager#validate(org.apache.rocketmq.acl.plain.PlainAccessResource)
 */
public class AclProducer {
    public static final String ACCESS_KEY = "rockettomcat";
    public static final String SECRET_KEY = "123456789";
    public static final String TOPIC = "aclTopic";
    public static final String TAGS = "aclTags";

    public static void main(String[] args) throws Exception {
        AclClientRPCHook rpcHook = new AclClientRPCHook(new SessionCredentials(ACCESS_KEY, SECRET_KEY));
        DefaultMQProducer producer = new DefaultMQProducer("rocketmq-zy-group-async", rpcHook);
        try {
            producer.setNamesrvAddr("192.168.0.156:9876");
            producer.start();
            Message message = new Message();
            message.setTopic(TOPIC);
            message.setTags(TAGS);
            message.setBody("acl-msg-1".getBytes(StandardCharsets.UTF_8));
            SendResult result = producer.send(message);
            System.out.println(result);
        } finally {
            producer.shutdown();
        }
    }
}
