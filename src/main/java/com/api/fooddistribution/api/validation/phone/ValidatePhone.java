package com.api.fooddistribution.api.validation.phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatePhone implements ConstraintValidator<ValidPhone, Object> {
    private String message;


    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.toString().trim().isEmpty()) {
            return true;
        }
        return validPhoneNumber(value.toString().trim());
    }


    public static String removeLeadingZeroes(String value) {
        if (value == null || value.trim().isEmpty()){
            return value;
        }

        StringBuilder sb = new StringBuilder(value);
        while (sb.length() > 0 && (sb.charAt(0) == '0' || sb.charAt(0) == '+')) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public static String formartedPhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()){
            return value;
        }
        value = removeLeadingZeroes(value);
        if (match254(value)) {
            return value;
        }
        return "254"+value;
    }

    public static boolean validPhoneNumber(String value) {
        if (value == null || value.trim().isEmpty()){
            return false;
        }

        String phone = formartedPhoneNumber(value);
        Pattern pattern = Pattern.compile("^[254\\s\\./0-9]{12}$");
        Pattern dummyPattern = Pattern.compile("^(\\d)(?!\\1+$)\\d{0,9}$");

        Matcher matcher = pattern.matcher(phone);
        Matcher matcherDummy = dummyPattern.matcher(phone);

        return !matcherDummy.matches() && matcher.matches();
    }

    private static boolean match254(String value) {
        if (value == null || value.trim().isEmpty()){
            return false;
        }

        return Pattern.compile("^[254\\s\\./0-9]{12}$").matcher(value).matches();
    }
}
