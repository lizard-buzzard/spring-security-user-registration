package com.lizard.buzzard.security;

import com.lizard.buzzard.persistence.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component("userToUserDetailsConverter")
public class UserToUserDetailsConverter implements Converter<User, UserDetails> {
    /**
     * We need to provide an implementation of the loadUserByUsername() method of UserDetailsService. But the challenge is
     * that the findByUsername() method of our UserService returns a User entity, while Spring Security expects a
     * UserDetails object from the loadUserByUsername() method.
     * We will create a converter for this to convert User to UserDetails implementation.
     * @param user
     * @return
     */
    @Override
    public UserDetails convert(User user) {
        UserSecurityDetailsImpl userDetails = new UserSecurityDetailsImpl();
        if(user != null) {
            Collection<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .flatMap(r -> r.getPrivileges().stream()).map(p -> new SimpleGrantedAuthority(p.getName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            userDetails.setAuthorities(authorities);
            userDetails.setUsername(user.getEmail());
            userDetails.setPassword(user.getPassword());
            userDetails.setEnabled(user.getEnabled());
        }
        return userDetails;
    }
}
