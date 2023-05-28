package ru.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class MedicationDto {
    @JsonProperty("name")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Name should contain only letters, numbers, '-', '_'")
    private String name;

    @JsonProperty("weight")
    @NotNull
    @Min(value = 1, message = "Weight should not be less than 0")
    private Integer weight;

    @JsonProperty("code")
    @NotNull
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code should contain only upper case letters, numbers, '_'")
    private String code;

    @JsonProperty("image")
    @NotNull
    @Pattern(regexp = "^[\\/\\w\\.-]+\\.(jpg|jpeg|png|gif|bmp)$", message = "Image should contain only path")
    private String image;
}
