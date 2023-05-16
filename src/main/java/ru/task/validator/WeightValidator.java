package ru.task.validator;

import org.springframework.stereotype.Component;
import ru.task.dto.DroneMainParameters;
import ru.task.exception.OverweightException;

@Component
public class WeightValidator implements Validator{
    @Override
    public void validate(DroneMainParameters droneMainParameters) {
        if (droneMainParameters.getWeight() > 500){
            throw new OverweightException("Drone with serial number " + droneMainParameters.getDroneSerialNumber() + "is overweight");
        }
    }
}
