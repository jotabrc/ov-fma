package io.github.jotabrc.ov_fma_user.validation;

import io.github.jotabrc.ov_fma_user.util.CheckString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidatorEmail implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (CheckString.isNullOrBlank(s)) return false;
        return EmailValidator.getInstance().isValid(s);
    }
}
