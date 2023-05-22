package ru.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    @JsonProperty("city")
    private String city;

    @JsonProperty("street")
    private String street;
}
