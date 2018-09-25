package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.dao.RoleRepository;
import com.lizard.buzzard.persistence.dao.TokenRepository;
import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.TokenStatus;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.persistence.model.VerificationToken;
import com.lizard.buzzard.web.dto.ViewFormChangePassword;
import com.lizard.buzzard.web.dto.ViewFormUser;
import com.lizard.buzzard.web.exception.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

import static com.lizard.buzzard.persistence.model.TokenStatus.*;

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

    @Autowired
    MessageSource messageSource;

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
        TokenStatus tokenStatus = getTokenStatus(dbToken);

        if (tokenStatus == TOKEN_VALUD) {
            VerificationToken verificationToken = dbToken.get();
            User user = verificationToken.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            tokenRepository.delete(verificationToken);
        }
        return tokenStatus;
    }

    private TokenStatus getTokenStatus(Optional<VerificationToken> dbToken) {
        TokenStatus tokenStatus = dbToken
                .map(t -> t.getExpirationDate())
                .map(d -> d.after(Date.from(Instant.now())))
                .map(b -> b == true ? TOKEN_VALUD : TOKEN_EXPIRED).orElse(TOKEN_INVALID);
        return tokenStatus;
    }

    @Override
    public User replaceUserPassword(ViewFormChangePassword viewFormChangePassword) {
        String email = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();

        User user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException(
                String.format("User identified by e-mail %s is not found", email)));

        user.setNewPassword(passwordEncoder.encode(viewFormChangePassword.getNewpassword()));   // QUESTION: ??? (CharSequence)
        userRepository.save(user);
        return user;
    }

    @Override
    public Model validateResetTokenAndReplacePassword(long id, String token, Locale locale, Model model) {
        Optional<VerificationToken> resetToken = tokenRepository.findByToken(token);

        TokenStatus tokenStatus = getTokenStatus(resetToken);

        if(tokenStatus.equals(TOKEN_VALUD) && resetToken.get().getUser().getId().equals(Long.valueOf(id))) {
            model.addAttribute("changePasswordTokenValid",
                    messageSource.getMessage("change.verification.token.isValid", new Object[]{}, locale)
            );

            VerificationToken verificationToken = resetToken.get();
            String email = verificationToken.getUser().getEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(()->new UsernameNotFoundException(
                            String.format("User identified by e-mail %s is not found", email)));
            user.setPassword(user.getNewPassword());
            user.setNewPassword(new String());

            userRepository.save(user);
            tokenRepository.delete(verificationToken);
        } else if (tokenStatus.equals(TOKEN_INVALID)) {
            model.addAttribute("changePasswordTokenInalid",
                    messageSource.getMessage("change.verification.token.isInvalid", new Object[]{}, locale)
            );
        } else if (tokenStatus.equals(TOKEN_EXPIRED)) {
            model.addAttribute("changePasswordTokenExpired",
                    messageSource.getMessage("change.verification.token.isExpired", new Object[]{}, locale)
            );
        }
        return model;
    }
}
