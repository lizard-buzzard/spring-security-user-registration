package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.User;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default boolean isUserExists(String email) {
        return findByEmail(email).orElse(null) == null ? false : true;
    }

    Optional<User> findByFirstnameAndLastnameAndEmail(String aSuper, String admin, String s);
}
