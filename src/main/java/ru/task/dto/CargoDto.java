package ru.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CargoDto {
    @JsonProperty("quantity")
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity should not be less than 1")
    private Integer quantity;

    @JsonProperty("weight")
    private Integer weight;

    @JsonProperty("medication")
    private MedicationDto medication;
}
