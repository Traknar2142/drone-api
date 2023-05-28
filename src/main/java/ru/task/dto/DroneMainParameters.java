package ru.task.dto;

import lombok.Getter;
import lombok.Setter;
import ru.task.enums.State;
import ru.task.model.Medication;

import java.util.Map;

@Getter
@Setter
public class DroneMainParameters {
    private String droneSerialNumber;
    private Integer weight;
    private State state;
    private Integer percentage;
    private Map<Medication, Integer> medications;
}
