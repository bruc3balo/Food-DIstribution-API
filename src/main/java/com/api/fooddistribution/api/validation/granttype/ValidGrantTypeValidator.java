package com.api.fooddistribution.api.validation.granttype;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidGrantTypeValidator implements ConstraintValidator<ValidGrantType, String> {
    @Override
    public void initialize(ValidGrantType constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValidGrantType(value);
    }

    public static boolean isValidGrantType(String value){
        if (value == null || value.trim().isEmpty())
            return true;
        if (value.trim().equals("client_credentials") || value.trim().equals("password") || value.trim().equals("refresh_token") || value.trim().equals("authorization_code"))
            return true;

        return false;
    }

}
