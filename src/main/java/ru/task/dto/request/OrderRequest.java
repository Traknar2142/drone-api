package ru.task.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.task.dto.CustomerDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @JsonProperty("customer")
    private CustomerDto customer;

    @JsonProperty("medicationRequests")
    @NotNull
    @Valid
    private List<MedicationRequest> medicationRequests;
}
