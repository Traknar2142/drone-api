package ru.task.validator;

import org.springframework.stereotype.Component;
import ru.task.dto.DroneMainParameters;
import ru.task.exception.LowPercentageException;

@Component
public class PercentageValidator implements Validator{
    private static final Integer EDGE_PERCENTAGE = 25;
    @Override
    public void validate(DroneMainParameters droneMainParameters) {
        if (droneMainParameters.getPercentage() < EDGE_PERCENTAGE){
            throw new LowPercentageException("Drone's charge is low: " + droneMainParameters.getPercentage());
        }
    }
}
