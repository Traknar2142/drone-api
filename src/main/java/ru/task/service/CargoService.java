package ru.task.service;

import ru.task.model.Cargo;

import java.util.Set;

public interface CargoService {
    void deleteCargo(Set<Cargo> cargos);
}
