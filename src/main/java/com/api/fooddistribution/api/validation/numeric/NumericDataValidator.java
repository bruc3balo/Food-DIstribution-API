package com.api.fooddistribution.api.validation.numeric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NumericDataValidator implements ConstraintValidator<ValidNumeric, String> {
    private static final Logger log = LoggerFactory.getLogger(NumericDataValidator.class);

    public static final String regex = "\\p{Digit}+";

    @Override
    public void initialize(ValidNumeric constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return validNumeric(value);
    }

    public static boolean validNumeric(String value) {
        if (value == null || value.trim().isEmpty())
            return true;
        return Pattern.compile(regex).matcher(value.replaceAll("\\s", "")).matches();
    }
}
