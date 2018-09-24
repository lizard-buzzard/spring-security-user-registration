package com.lizard.buzzard.security;

import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class CustomRememberMeServices extends PersistentTokenBasedRememberMeServices {

    @Autowired
    private UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
    private PersistentTokenRepository tokenRepository;
    private String key;

    public CustomRememberMeServices(String key, String rememberMeServicesName, UserDetailsService userDetailsService, PersistentTokenRepository persistentTokenRepository) {
        super(key, userDetailsService, persistentTokenRepository);
//        String param = super.getParameter();
        this.setParameter(rememberMeServicesName);
        this.tokenRepository = persistentTokenRepository;
        this.key = key;
    }

    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        String username = ((User) successfulAuthentication.getPrincipal()).getEmail();
        logger.debug("Creating new persistent login for user " + username);
        PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, generateSeriesData(), generateTokenData(), new Date());
        try {
            // TODO: check before if user already exists
            tokenRepository.createNewToken(persistentToken);
            addCookie(persistentToken, request, response);
        } catch (Exception e) {
            logger.error("Failed to save persistent token ", e);
        }
    }

    @Override
    protected Authentication createSuccessfulAuthentication(HttpServletRequest request, UserDetails user) {
        User auser = userRepository.findByEmail(user.getUsername()).orElse(null);

        RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(key, auser, authoritiesMapper.mapAuthorities(user.getAuthorities()));
        auth.setDetails(authenticationDetailsSource.buildDetails(request));
        return auth;
    }

    private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[] { token.getSeries(), token.getTokenValue() }, getTokenValiditySeconds(), request, response);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
        if (authentication != null) {
            String username = ((User) authentication.getPrincipal()).getEmail();
            this.tokenRepository.removeUserTokens(username);
        }
    }
}
