package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = FuturePlusHoursValidator.class)
@Documented
public @interface FuturePlusHours {
    String message() default "Date must be at least 2 hours in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
