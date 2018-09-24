package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.dao.RoleRepository;
import com.lizard.buzzard.persistence.dao.TokenRepository;
import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.TokenStatus;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.persistence.model.VerificationToken;
import com.lizard.buzzard.web.dto.ViewFormUser;
import com.lizard.buzzard.web.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static com.lizard.buzzard.persistence.model.TokenStatus.*;

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

    @Autowired
    private TokenRepository tokenRepository;

    @Value("${lizard.verivication.token.expiration}")
    private Long tokenExpiration;

    @Override
    public User saveUserInRepository(ViewFormUser dtoUser, Locale locale) {
        if (userRepository.isUserExists(dtoUser.getEmail())) {
            throw new UserAlreadyExistException(
                    String.format("User with email \"%s\" already exists", dtoUser.getEmail()), locale);
        }

        User user = new User();
        user.setFirstname(dtoUser.getFirstname());
        user.setLastname(dtoUser.getLastname());
        user.setEmail(dtoUser.getEmail());
        user.setPassword(passwordEncoder.encode(dtoUser.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByRolename("ROLE_USER")));
        userRepository.save(user);
        return user;
    }

    @Override
    public void createUsersToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user, this.tokenExpiration);
        tokenRepository.save(verificationToken);
    }

    @Override
    public TokenStatus verifyConfirmationToken(String token) {
        Optional<VerificationToken> dbToken = tokenRepository.findByToken(token);
        TokenStatus tokenStatus = dbToken
                .map(t -> t.getExpirationDate())
                .map(d -> d.after(Date.from(Instant.now())))
                .map(b -> b == true ? TOKEN_VALUD : TOKEN_EXPIRED).orElse(TOKEN_INVALID);

        if (tokenStatus == TOKEN_VALUD) {
            User user = dbToken.get().getUser();
            user.setEnabled(true);
            userRepository.save(user);
        }
        return tokenStatus;
    }
}
