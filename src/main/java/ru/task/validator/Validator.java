package ru.task.validator;

import ru.task.dto.DroneMainParameters;

public interface Validator {
    void validate(DroneMainParameters droneMainParameters);
}
