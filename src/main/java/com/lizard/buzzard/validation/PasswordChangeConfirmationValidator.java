package com.lizard.buzzard.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordChangeConfirmationValidatorImpl.class)
public @interface PasswordChangeConfirmationValidator {

    String message() default "{constraint.validator.user.password.confirmation}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
