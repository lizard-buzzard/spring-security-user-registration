package com.lizard.buzzard.validation;


import com.lizard.buzzard.web.dto.ViewFormChangePassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordChangeConfirmationValidatorImpl implements ConstraintValidator<PasswordChangeConfirmationValidator, Object> {
    @Override
    public void initialize(PasswordChangeConfirmationValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object viewFormChangePassword, ConstraintValidatorContext constraintValidatorContext) {
        ViewFormChangePassword changePasswordDto = (ViewFormChangePassword)viewFormChangePassword;
        return changePasswordDto.getConfirmedpassword().equals(changePasswordDto.getNewpassword());
    }
}
