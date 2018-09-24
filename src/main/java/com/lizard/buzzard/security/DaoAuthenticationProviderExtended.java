package com.lizard.buzzard.security;

import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class DaoAuthenticationProviderExtended extends DaoAuthenticationProvider {
    private final Logger LOGGER = LoggerFactory.getLogger(DaoAuthenticationProviderExtended.class);

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
//        String test = ((User) authentication.getPrincipal()).getEmail();

        String userEmail = auth.getName();      // email serves as a name
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new BadCredentialsException("Bad password or email"));

        LOGGER.debug("==> DaoAuthenticationProviderExtended.uthenticate(): BEFORE Authentication result = super.authenticate(auth);");

        Authentication result = super.authenticate(auth);

        LOGGER.debug("==> DaoAuthenticationProviderExtended.uthenticate(): AFTER Authentication result = super.authenticate(auth);");

        return new UsernamePasswordAuthenticationToken(
                user,                           // principal
                result.getCredentials(),        // credentials
                result.getAuthorities()
        );
    }
}
