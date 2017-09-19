package com.brainplugs.red.squirrel.redsquirrelserver.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@RedisHash("User")
public class User implements MessageListener {

    @Id
    private String id;
    @Indexed
    private String alias;
    private String email;
    private Map<String, Boolean> pushNotificationsByChannel = new HashMap<>();
    private Set<ChannelTopic> channelTopics = new HashSet<>();
    private boolean active;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        if(active) {
            JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
            RedSquirrelMessage redSquirrelMessage = (RedSquirrelMessage) jdkSerializationRedisSerializer.deserialize(message.getBody());
            System.out.println("Receiver alias: " + alias);
            System.out.println("Sender: " + redSquirrelMessage.getSenderId());
            System.out.println("Message Timestamp : " + redSquirrelMessage.getTimestamp().toString());
            System.out.println("Message:" + redSquirrelMessage.getMessage());
            System.out.println("Channel:" + redSquirrelMessage.getTopic());
        }
    }


}