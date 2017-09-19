package com.brainplugs.red.squirrel.redsquirrelserver.repository;

import com.brainplugs.red.squirrel.redsquirrelserver.models.RedSquirrelMessage;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RSMRepository extends PagingAndSortingRepository<RedSquirrelMessage, String> {
}
