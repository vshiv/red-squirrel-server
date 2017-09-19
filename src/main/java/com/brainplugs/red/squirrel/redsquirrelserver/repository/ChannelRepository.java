package com.brainplugs.red.squirrel.redsquirrelserver.repository;


import com.brainplugs.red.squirrel.redsquirrelserver.models.Channel;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChannelRepository extends PagingAndSortingRepository<Channel, String> {

    Channel findByTopic(String topic);
}
