package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.dao.RoleRepository;
import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.web.dto.ViewFormUser;
import com.lizard.buzzard.web.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

/**
 * Implementation of the service for registering account
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public User saveUserInRepository(ViewFormUser dtoUser) {
        if (userRepository.isUserExists(dtoUser.getEmail())) {
            throw new UserAlreadyExistException(
                    String.format("User with email \"%s\" already exists", dtoUser.getEmail()));
        }

        User user = new User();
        user.setFirstname(dtoUser.getFirstname());
        user.setLastname(dtoUser.getLastname());
        user.setEmail(dtoUser.getEmail());
        user.setPassword(passwordEncoder.encode(dtoUser.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByRolename("USER")));
        userRepository.save(user);
        return user;
    }


}
