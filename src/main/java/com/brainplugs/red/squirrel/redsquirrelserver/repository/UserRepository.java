package com.brainplugs.red.squirrel.redsquirrelserver.repository;

import com.brainplugs.red.squirrel.redsquirrelserver.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, String> {

    User findByAlias(String alias);
}
