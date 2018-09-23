package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.User;

public interface UserRepository extends BaseRepository<User, Long> {
    User findByEmail(String email);

    default boolean isUserExists(String email) {
        return findByEmail(email) == null ? false : true;
    }
}
