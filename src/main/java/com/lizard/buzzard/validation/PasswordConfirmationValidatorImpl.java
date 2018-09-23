package com.lizard.buzzard.validation;

import com.lizard.buzzard.web.dto.ViewFormUser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConfirmationValidatorImpl implements ConstraintValidator<PasswordConfirmationValidator, Object> {
    @Override
    public void initialize(PasswordConfirmationValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object viewFormUser, ConstraintValidatorContext constraintValidatorContext) {
        ViewFormUser userDto = (ViewFormUser)viewFormUser;
        return userDto.getPasswordconfirmed().equals(userDto.getPassword());
    }
}
