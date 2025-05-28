package io.github.jotabrc.ov_fma_user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidatorEmail.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {
    String message() default "Invalid EMAIL format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
