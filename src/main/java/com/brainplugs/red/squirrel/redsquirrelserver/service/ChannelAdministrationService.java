package com.brainplugs.red.squirrel.redsquirrelserver.service;

import com.brainplugs.red.squirrel.redsquirrelserver.models.Channel;
import com.brainplugs.red.squirrel.redsquirrelserver.models.User;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.ChannelRepository;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class ChannelAdministrationService {
    private ChannelRepository channelRepository;
    private UserRepository userRepository;
    private RedisMessageListenerContainer container;

    @Autowired
    public ChannelAdministrationService(final ChannelRepository repository, final UserRepository userRepository, RedisMessageListenerContainer container) {
        this.channelRepository = repository;
        this.userRepository = userRepository;
        this.container = container;
    }

    /**
     * Change the topic topic for the topic matching the supplied old topic with the supplied new one
     *
     * @param oldTopic Old topic topic
     * @param newTopic New topic topic
     */
    public boolean changeTopic(final @NonNull String oldTopic, final @NonNull String newTopic) {
        final Channel channel = channelRepository.findByTopic(oldTopic);
        if(channel == null)
            return false;
        channel.setTopic(newTopic);
        channelRepository.save(channel);
        return true;
    }

    public boolean addUser(final @NonNull String topic, final @NonNull String userId, final boolean enablePushNotification) {
        final Channel channel = channelRepository.findByTopic(topic);
        if(channel == null)
            return false;
        final User user = userRepository.findByAlias(userId);
        if(user == null)
            return false;
        channelRepository.save(channel);
        container.addMessageListener(new MessageListenerAdapter(user), channel);
        Set<ChannelTopic> userTopics = user.getChannelTopics();
        userTopics.add(channel);
        user.setChannelTopics(userTopics);
        user.getPushNotificationsByChannel().putIfAbsent(topic, enablePushNotification);
        userRepository.save(user);
        return true;
    }



    public String add(final @NonNull String topic) {
        if(!channelRepository.exists(topic)) {
            Channel channel = new Channel(topic);
            channelRepository.save(channel);
        }
        return null;
    }

    public void delete(final String topic) {
        final Channel channel = channelRepository.findByTopic(topic);
        channelRepository.delete(channel);
    }

    public void deleteUser(String topic, String userId) {
        final Channel channel = channelRepository.findByTopic(topic);
        final User user = userRepository.findByAlias(userId);
        container.removeMessageListener(user, channel);
        user.getChannelTopics().remove(channel);
        userRepository.save(user);
    }

    public String fetchByTopic(final String topic) {
        Channel channel = channelRepository.findByTopic(topic);
        return channel.getTopic();
    }
}
