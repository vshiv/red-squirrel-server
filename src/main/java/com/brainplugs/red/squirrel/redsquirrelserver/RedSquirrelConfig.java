package com.brainplugs.red.squirrel.redsquirrelserver;

import com.brainplugs.red.squirrel.redsquirrelserver.repository.ChannelRepository;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.UserRepository;
import com.brainplugs.red.squirrel.redsquirrelserver.service.UserProxyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRedisRepositories
public class RedSquirrelConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    @Bean
    RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName(host);
        factory.setPort(port);
        return factory;
    }

    @Bean
    RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactory connectionFactory, Map<String, UserProxyService> userProxyServiceMap,
                                                 SimpMessagingTemplate simpMessagingTemplate,
                                                 ChannelRepository channelRepository, UserRepository userRepository) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        userRepository.findAll().forEach(user -> {
            UserProxyService userProxyService = new UserProxyService(user.getAlias(), simpMessagingTemplate, channelRepository, userRepository);
            userProxyServiceMap.putIfAbsent(user.getId(), userProxyService);
            user.getChannelTopics().forEach(topic -> {
                MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(userProxyService);
                container.addMessageListener(messageListenerAdapter, channelRepository.findByTopic(topic));
            });
        });
        return container;
    }

    @Bean
    Map<String, UserProxyService> userProxyServiceMap(){
        return new HashMap<>();
    }

}
