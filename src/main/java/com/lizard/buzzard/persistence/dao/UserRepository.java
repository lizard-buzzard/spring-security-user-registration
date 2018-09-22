package com.lizard.buzzard.persistence.dao;

import com.lizard.buzzard.persistence.model.User;

public interface UserRepository extends BaseRepository<User, Long> {
    User findByEmail(String email);
}
