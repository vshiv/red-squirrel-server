package com.brainplugs.red.squirrel.redsquirrelserver.service;

import com.brainplugs.red.squirrel.redsquirrelserver.models.RedSquirrelMessage;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.RSMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class MessagingService {

    private RSMRepository rsmRepository;
    private RedisTemplate redisTemplate;

    @Autowired
    public MessagingService(final RSMRepository repository, RedisTemplate redisTemplate) {
        this.rsmRepository = repository;
        this.redisTemplate = redisTemplate;
    }

    public void publish(final String userId, final String topic, final String message) {
        final RedSquirrelMessage rsm = new RedSquirrelMessage();
        rsm.setMessage(message);
        rsm.setSenderId(userId);
        rsm.setTimestamp(Instant.now());
        rsm.setTopic(topic);
        redisTemplate.convertAndSend(topic, rsm);
        rsmRepository.save(rsm);
    }
}
