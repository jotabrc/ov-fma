package io.github.jotabrc.ov_fma_user.validation;

import io.github.jotabrc.ov_fma_user.util.CheckString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorUsername implements ConstraintValidator<ValidName, String> {

    @Value("${ov.fma.common.validation.pattern.username}")
    private String regex;

    @Override
    public void initialize(ValidName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (CheckString.isNullOrBlank(s)) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
