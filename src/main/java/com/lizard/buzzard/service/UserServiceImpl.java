package com.lizard.buzzard.service;


import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.web.dto.ViewFormUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of the service for registering account
 */
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User saveRegisteredUser(ViewFormUser dtoUser) {
        User user = new User();
        user.setFirstname(dtoUser.getFirstname());
        user.setPassword(dtoUser.getPassword());
//        user.set
//        userRepository.save(user);
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User findByLogin(String email) {
        return null;
    }
}
