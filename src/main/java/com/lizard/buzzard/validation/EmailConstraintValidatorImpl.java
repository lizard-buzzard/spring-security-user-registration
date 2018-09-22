package com.lizard.buzzard.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailConstraintValidatorImpl implements ConstraintValidator<EmailConstraintValidator, String> {
    @Override
    public void initialize(EmailConstraintValidator constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
