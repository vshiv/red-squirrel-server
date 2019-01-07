package com.brainplugs.red.squirrel.redsquirrelserver.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.redis.listener.ChannelTopic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@RedisHash("Channel")
public class Channel extends ChannelTopic{
	@Id
	private String id;
    @Indexed
	private String topic;
    private Set<RedSquirrelMessage> messages;

    /**
     * Constructs a new <code>ChannelTopic</code> instance.
     *
     * @param topic
     */
    public Channel(String topic) {
        super(topic);
        this.topic = topic;
        this.messages = new HashSet<>();
    }
}
