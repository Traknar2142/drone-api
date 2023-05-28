package ru.task.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IdleStateValidator.class})
public @interface IdleState {
    String message() default "Drone must be in IDLE state";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
