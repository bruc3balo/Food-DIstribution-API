package com.api.fooddistribution.api.validation.phone;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidatePhone.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidPhone {
    String message() default "Invalid Phone number 254XXXXXXXXX";
    Class<?>[] groups() default  {};
    Class<? extends Payload>[] payload() default {};

}



