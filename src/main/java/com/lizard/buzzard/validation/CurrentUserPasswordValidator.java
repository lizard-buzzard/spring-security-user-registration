package com.lizard.buzzard.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrentUserPasswordValidatorImpl.class)
public @interface CurrentUserPasswordValidator {
    String message() default "{constraint.validator.current.user.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
