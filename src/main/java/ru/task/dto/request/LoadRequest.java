package ru.task.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class LoadRequest {
    @JsonProperty("droneSerialNumber")
    private String droneSerialNumber;
    @JsonProperty("medicationRequests")
    @NotNull
    @Valid
    private List<MedicationRequest> medicationRequests;
}
