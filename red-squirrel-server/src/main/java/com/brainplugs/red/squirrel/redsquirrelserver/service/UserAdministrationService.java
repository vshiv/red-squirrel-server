package com.brainplugs.red.squirrel.redsquirrelserver.service;

import com.brainplugs.red.squirrel.redsquirrelserver.models.User;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.ChannelRepository;
import com.brainplugs.red.squirrel.redsquirrelserver.repository.UserRepository;
import com.google.common.collect.Lists;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;


@Service
public class UserAdministrationService {

    private UserRepository userRepository;
    private ChannelRepository channelRepository;

    @Autowired
    public UserAdministrationService(final UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    /**
     * Fetch all the active User
     * @return All the {@link User users} present in the repository
     */
    public List<User> fetchAllUsers() {
        return Lists.newArrayList(userRepository.findAll());
    }

    public void add(final @NonNull String userName, final @NonNull String email) {
        final User user = new User();
        user.setAlias(userName);
        user.setEmail(email);
        user.setChannelTopics(new HashSet<>());
        user.setActive(true);
        userRepository.save(user);
    }

    public void delete(final String userName) {
        final User user = userRepository.findByAlias(userName);
        user.setActive(false);
    }

    public void updateUserDetails(final @NonNull String userName, final @NonNull String newEmail) {
        final User user = userRepository.findByAlias(userName);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public User fetch(String userName) {
        return userRepository.findByAlias(userName);
    }
}
