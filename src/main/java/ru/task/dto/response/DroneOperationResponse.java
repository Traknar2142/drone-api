package ru.task.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DroneOperationResponse {
    @JsonProperty("serialNumber")
    private final String serialNumber;
    @JsonProperty("message")
    private final String message;
}
