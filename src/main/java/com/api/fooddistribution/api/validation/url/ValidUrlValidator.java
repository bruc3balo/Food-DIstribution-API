package com.api.fooddistribution.api.validation.url;

import org.apache.commons.validator.UrlValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidUrlValidator implements ConstraintValidator<ValidUrl, String> {

    public void initialize(ValidUrl constraintAnnotation) {  }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty())
            return true;
        return validateUrl(value);
    }

    public static boolean validateUrl(String value) {
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(value);
    }

}