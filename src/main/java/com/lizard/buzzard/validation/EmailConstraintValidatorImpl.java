package com.lizard.buzzard.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailConstraintValidatorImpl implements ConstraintValidator<EmailConstraintValidator, String> {

    private final Pattern VALID_EMAIL_FORMAT_PATTERN = Pattern.compile(
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    @Override
    public void initialize(EmailConstraintValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return VALID_EMAIL_FORMAT_PATTERN.matcher(email).matches();
    }
}
