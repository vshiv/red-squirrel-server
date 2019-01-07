package com.brainplugs.red.squirrel.redsquirrelserver.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@RedisHash("User")
public class User {
    @Id
    private String id;
    @Indexed
    private String alias;
    private String email;
    private Map<String, Boolean> pushNotificationsByChannel = new HashMap<>();
    private Set<String> channelTopics = new HashSet<>();
    private boolean active;
}