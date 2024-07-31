package ru.practicum.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class FuturePlusHoursValidator implements ConstraintValidator<FuturePlusHours, LocalDateTime> {
    @Override
    public void initialize(FuturePlusHours constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime nowPlusTwoHours = LocalDateTime.now().plusHours(2);
        return value.isAfter(nowPlusTwoHours);
    }
}
