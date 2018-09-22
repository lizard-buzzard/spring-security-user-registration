package com.lizard.buzzard.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Spring MVC Custom Validation. SEE: https://www.baeldung.com/spring-mvc-custom-validator
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailConstraintValidatorImpl.class)
public @interface EmailConstraintValidator {

    /**
     * Normally error message keys are being searched in the file called ValidationMessages.properties
     * that should be available on the application class path
     * SEE: http://dolszewski.com/spring/custom-validation-annotation-in-spring/
     * NOTE: but in our case it was sufficient to define message key in Thymeleaf's messages_xx.properties
     *
     * @return internationalized message from Thymeleaf's messages_xx.properties by the key
     */
    String message() default "{constraint.validator.user.registration.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
