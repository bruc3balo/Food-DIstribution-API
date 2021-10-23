package com.api.fooddistribution.api.validation.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {

    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty())
            return true;

        return (validateEmail(email));
    }

    public static boolean validateEmail(final String email) {
        return  Pattern.compile("^[A-Z0-9'._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", 2).matcher(email).matches();
    }

}