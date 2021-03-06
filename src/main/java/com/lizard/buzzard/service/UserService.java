package com.lizard.buzzard.service;

import com.lizard.buzzard.persistence.model.TokenStatus;
import com.lizard.buzzard.persistence.model.User;
import com.lizard.buzzard.web.dto.ViewFormChangePassword;
import com.lizard.buzzard.web.dto.ViewFormUser;
import org.springframework.ui.Model;

import java.util.Locale;

/**
 * Provide service for registering account
 */
public interface UserService {

    User saveUserInRepository(ViewFormUser dtoUser, Locale locale);

    void createUsersToken(User user, String token);

    TokenStatus verifyConfirmationToken(String token);

    User replaceUserPassword(ViewFormChangePassword viewFormChangePassword);

    Model validateResetTokenAndReplacePassword(long id, String token, Locale locale, Model model);
}
