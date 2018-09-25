package com.lizard.buzzard.validation;

import com.lizard.buzzard.persistence.dao.UserRepository;
import com.lizard.buzzard.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrentUserPasswordValidatorImpl implements ConstraintValidator<CurrentUserPasswordValidator, String> {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;
    
    @Override
    public void initialize(CurrentUserPasswordValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String passwordProvidedAsPasswordOfCurrentUser, ConstraintValidatorContext constraintValidatorContext) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String connectedUserPassword = principal.getPassword();
        return passwordEncoder.matches(passwordProvidedAsPasswordOfCurrentUser, connectedUserPassword);
    }
}
