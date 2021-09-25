package com.api.fooddistribution.api.validation.amount;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Constraint(validatedBy = AmountValidator.class)
public @interface ValidAmount {
    String message() default "Valid Amount required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
