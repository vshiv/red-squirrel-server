package com.brainplugs.red.squirrel.redsquirrelserver.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.time.Instant;

@EqualsAndHashCode
@Getter
@Setter
@RedisHash("RSMessage")
public class RedSquirrelMessage implements Message {
    @Id
    private String messageId;
    private String message;
    private String senderId;
    private Instant timestamp;
    private String topic;

    @Override
    public byte[] getBody() {
        return new JdkSerializationRedisSerializer().serialize(this);
    }

    @Override
    public byte[] getChannel() {
        return topic.getBytes();
    }
}
