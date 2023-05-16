package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.model.Cargo;
import ru.task.repository.CargoRepository;

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
}
