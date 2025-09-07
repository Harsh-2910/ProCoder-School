package com.example.school.annotation;

import com.example.school.validations.PasswordStrengthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface PasswordValidator {
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String message() default "Please enter strong password";
}
