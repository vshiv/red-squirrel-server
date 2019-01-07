package com.brainplugs.red.squirrel.redsquirrelserver.service;

import com.brainplugs.red.squirrel.redsquirrelserver.models.Channel;
import com.brainplugs.red.squirrel.redsquirrelserver.models.RedSquirrelMessage;
import com.brainplugs.red.squirrel.redsquirrelserver.models.User;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.ChannelRepository;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class UserProxyService implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(UserProxyService.class);
    private String alias;
    private final SimpMessagingTemplate webSocket;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public UserProxyService(String alias, SimpMessagingTemplate messagingTemplate, ChannelRepository repository, UserRepository userRepository) {
        this.alias = alias;
        this.webSocket = messagingTemplate;
        this.channelRepository = repository;
        this.userRepository = userRepository;
    }

        @Override
        public void onMessage(Message message, byte[] pattern) {
            User user = userRepository.findByAlias(alias);
            if(user.isActive()) {
                JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
                RedSquirrelMessage redSquirrelMessage = (RedSquirrelMessage) jdkSerializationRedisSerializer.deserialize(message.getBody());
                webSocket.convertAndSend("/"+redSquirrelMessage.getTopic(), redSquirrelMessage);
                Channel topic = channelRepository.findByTopic(redSquirrelMessage.getTopic());
                topic.getMessages().add(redSquirrelMessage);
                channelRepository.save(topic);
                logger.debug("Receiver alias: " + user.getAlias());
                logger.debug("Sender: " + redSquirrelMessage.getSenderId());
                logger.debug("Message Timestamp : " + redSquirrelMessage.getTimestamp().toString());
                logger.debug("Message:" + redSquirrelMessage.getMessage());
                logger.debug("Channel:" + redSquirrelMessage.getTopic());
            }
    }
}
