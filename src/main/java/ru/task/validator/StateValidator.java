package ru.task.validator;

import org.springframework.stereotype.Component;
import ru.task.dto.DroneMainParameters;
import ru.task.enums.State;
import ru.task.exception.DroneStateException;

@Component
public class StateValidator implements Validator{
    @Override
    public void validate(DroneMainParameters droneMainParameters) {
        if (!droneMainParameters.getState().equals(State.IDLE)
            && !droneMainParameters.getState().equals(State.LOADING)){
            throw new DroneStateException("Inappropriate state of drone: " + droneMainParameters.getState());
        }
    }
}
