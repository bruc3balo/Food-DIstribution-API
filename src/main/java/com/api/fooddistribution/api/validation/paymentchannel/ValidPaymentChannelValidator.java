package com.api.fooddistribution.api.validation.paymentchannel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;


public class ValidPaymentChannelValidator implements ConstraintValidator<ValidPaymentChannel, String> {
    @Override
    public void initialize(ValidPaymentChannel constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValidGrantType(value);
    }

    public static boolean isValidGrantType(String value){
        if (value == null || value.trim().isEmpty())
            return true;
        return Arrays.asList("STK_PUSH","CARD","AIRTEL","EQUITEL").contains(value.trim().toUpperCase());// value.trim().equals("STK_PUSH") || value.trim().equals("CARD") || value.trim().equals("EXPRESS_MPESA");
    }

}
