package com.api.fooddistribution.api.validation.url;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy={ValidUrlValidator.class})
@Target(value={ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface ValidUrl {
    String message() default "Invalid Url";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
