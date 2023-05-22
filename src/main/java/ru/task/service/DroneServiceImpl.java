package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.enums.State;
import ru.task.model.Drone;
import ru.task.repository.DroneRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class DroneServiceImpl implements DroneService{
    private final DroneRepository droneRepository;

    public DroneServiceImpl(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    @Transactional
    public Drone saveDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    @Override
    public List<Drone> saveAll(List<Drone> drones) {
        return droneRepository.saveAll(drones);
    }

    @Override
    @Transactional(readOnly = true)
    public Drone findBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EntityNotFoundException("Drone with serial number " + serialNumber + " is not found"));
    }

    @Override
    public List<Drone> findByState(State state) {
        return droneRepository.findByState(state);
    }

    @Override
    public List<Drone> findByStateAndPercentageGreaterThan(State state, Integer percentage) {
        return droneRepository.findByStateAndPercentageGreaterThan(state, percentage);
    }

    @Override
    public List<Drone> findByStateIn(List<State> states) {
        return droneRepository.findByStateIn(states);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Drone> findAll() {
        return droneRepository.findAll();
    }

    @Override
    @Transactional
    public Drone updateDrone(Drone drone) {
        return droneRepository.save(drone);
    }

    @Override
    @Transactional
    public void deleteDroneBySerialNumber(String serialNumber) {
        droneRepository.deleteBySerialNumber(serialNumber);
    }

}
