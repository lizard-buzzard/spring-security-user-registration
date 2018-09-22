package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.web.dto.ViewFormUser;

/**
 * Provides service for registering account
 */
public interface UserService {

    User saveRegisteredUser(ViewFormUser dtoUser);

    User save(User user);

    User findByLogin(String login);

}
