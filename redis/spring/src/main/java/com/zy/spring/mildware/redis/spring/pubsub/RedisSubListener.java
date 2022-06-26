package com.zy.spring.mildware.redis.spring.pubsub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

/**
 * redis sub 消息
 */
@Configuration
public class RedisSubListener extends MessageListenerAdapter {

    public static final String REDIS_CHANNEL_01 = "redis_channel_01";
    public static final String REDIS_CHANNEL_02 = "redis_channel_02";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] body = message.getBody();
        byte[] channel = message.getChannel();
        String msg = stringRedisTemplate.getStringSerializer().deserialize(body);
        String topic = stringRedisTemplate.getStringSerializer().deserialize(channel);
        System.out.println(String.format("msg: %s --> channel: %s.", msg, topic));
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(this, ChannelTopic.of(REDIS_CHANNEL_01));
        // container.addMessageListener(this, ChannelTopic.of(REDIS_CHANNEL_02)); // 如果注掉这里, 则不订阅该 topic 消息
        return container;
    }

}
