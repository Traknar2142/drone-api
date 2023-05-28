package ru.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDto {
    @JsonProperty("name")
    private String name;

    @JsonProperty("address")
    private AddressDto address;
}
