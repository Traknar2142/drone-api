package ru.task.dto;

import lombok.Getter;
import lombok.Setter;
import ru.task.model.Cargo;
import ru.task.model.Customer;

import java.util.Set;

@Getter
@Setter
public class OrderDto {
    private Long Id;

    private Customer customer;

    private Set<Cargo> cargos;
}
