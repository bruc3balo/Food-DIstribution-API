package com.api.fooddistribution.api.validation.date.past;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidFutureDateValidator implements ConstraintValidator<ValidFutureDate, String> {
    private String message;

    @Override
    public void initialize(ValidFutureDate constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.trim().isEmpty())
            return true;

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fieldValDate = dateFormat.parse(value.trim());
            Date dateNow = dateFormat.parse(dateFormat.format(new Date()));

            if (fieldValDate.compareTo(dateNow) < 0){
                return false;
            }
            return true;
        }catch (Exception ex){
//            ex.printStackTrace();
            return true;
        }
    }

    public static boolean dateInfuture(String value) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date fieldValDate = dateFormat.parse(value.trim());
            Date dateNow = dateFormat.parse(dateFormat.format(new Date()));

            if (fieldValDate.compareTo(dateNow) < 0){
                return false;
            }
            return true;
        }catch (Exception ex){
//            ex.printStackTrace();
            return true;
        }
    }
}
