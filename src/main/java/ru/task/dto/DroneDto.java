package ru.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.task.enums.Model;
import ru.task.enums.State;
import ru.task.validator.IdleState;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class DroneDto {
    @JsonProperty("serialNumber")
    @Size(max = 100, message = "Serial number cannot be longer than 100 characters")
    @NotNull
    private String serialNumber;

    @JsonProperty("model")
    @NotNull
    private Model model;

    @JsonProperty("weight")
    @Min(value = 1, message = "Weight cannot be less than 1")
    @Max(value = 500, message = "Weight limit cannot exceed 500 grams")
    @NotNull
    private Integer weight;

    @JsonProperty("percentage")
    @Min(value = 0, message = "Percentage cannot be less than 0")
    @Max(value = 100, message = "Percentage cannot be greater than 100")
    @NotNull
    private Integer percentage;

    @JsonProperty("state")
    @IdleState
    @NotNull
    private State state;

    private Set<CargoDto> cargo;

}
