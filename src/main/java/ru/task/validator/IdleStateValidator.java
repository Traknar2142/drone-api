package ru.task.validator;

import ru.task.enums.State;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdleStateValidator implements ConstraintValidator<IdleState, State> {
    @Override
    public void initialize(IdleState constraintAnnotation) {
    }

    @Override
    public boolean isValid(State state, ConstraintValidatorContext constraintValidatorContext) {
        return state.equals(State.IDLE);
    }
}
