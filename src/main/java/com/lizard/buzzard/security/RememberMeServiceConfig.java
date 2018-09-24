package com.lizard.buzzard.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
public class RememberMeServiceConfig {

    @Bean
    public CustomRememberMeServices rememberMeServices(
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            @Qualifier("persistentTokenRepository") PersistentTokenRepository persistentTokenRepository) {
        CustomRememberMeServices rememberMeServices = new CustomRememberMeServices("theKey", userDetailsService, persistentTokenRepository);
        return rememberMeServices;
    }
}
