package com.api.fooddistribution.api.validation.paymentchannel;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Documented
@Constraint(validatedBy = ValidPaymentChannelValidator.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPaymentChannel {
    String message() default "Valid Payment Channel required STK_PUSH,CARD,AIRTEL,EQUITEL" ;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
