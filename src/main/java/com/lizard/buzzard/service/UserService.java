package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.model.User;

/**
 * Provide service for registering account
 */
public interface UserService {
    User save(User user);

    User findByLogin(String login);

}
