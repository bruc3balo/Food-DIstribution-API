package com.api.fooddistribution.api.validation.amount;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount, String> {

    @Override
    public void initialize(ValidAmount constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return validAmount(value);
    }

    public static boolean validAmount(String value){
        if (value == null || value.trim().isEmpty())
            return true;

        try {
            Double d3 = Double.valueOf(value);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
