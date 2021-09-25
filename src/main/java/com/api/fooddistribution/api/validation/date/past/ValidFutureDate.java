package com.api.fooddistribution.api.validation.date.past;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = ValidFutureDateValidator.class)
@Target({ANNOTATION_TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFutureDate {
    String message() default "Date cannot be in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
