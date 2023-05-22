package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.model.Cargo;
import ru.task.model.Medication;
import ru.task.repository.CargoRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CargoServiceImpl implements CargoService{
    private final CargoRepository cargoRepository;

    public CargoServiceImpl(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @Override
    @Transactional
    public void deleteCargo(Set<Cargo> cargos) {
        cargoRepository.deleteAll(cargos);
    }

    @Override
    public Set<Cargo> createCargo(Map<Medication, Integer> map) {
        Set<Cargo> cargos = new HashSet<>();
        map.forEach((med, quantity) -> cargos.add(new Cargo(quantity, med.getWeight() * quantity, med)));
        return cargos;
    }
}
