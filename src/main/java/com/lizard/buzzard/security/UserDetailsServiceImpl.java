package com.lizard.buzzard.security;

import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    @Qualifier("userToUserDetailsConverter")
    UserToUserDetailsConverter converter;

    @Autowired
    UserRepository userRepository;

    public UserDetailsServiceImpl() {
        super();
    }

    /**
     * We need to provide an implementation of the loadUserByUsername() method of UserDetailsService. But the challenge is
     * that the findByUsername() method of our UserService returns a User entity, while Spring Security expects a
     * UserDetails object from the loadUserByUsername() method.
     * We will create a converter for this to convert User to UserDetails implementation.
     *
     * @param userEmail
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new UsernameNotFoundException(
                        String.format("User identified by e-mail %s is not found", userEmail)));
        UserDetails userdetails = converter.convert(user);
        return userdetails;
    }
}
