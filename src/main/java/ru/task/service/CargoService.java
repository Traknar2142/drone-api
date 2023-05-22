package ru.task.service;

import ru.task.model.Cargo;
import ru.task.model.Medication;

import java.util.Map;
import java.util.Set;

public interface CargoService {
    void deleteCargo(Set<Cargo> cargos);
    Set<Cargo> createCargo(Map<Medication, Integer> medication);
}
