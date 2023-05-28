package ru.task.service;

import ru.task.enums.State;
import ru.task.model.Drone;

import java.util.List;

public interface DroneService {
    Drone saveDrone(Drone drone);
    List<Drone> saveAll(List<Drone> drones);
    Drone findBySerialNumber(String serialNumber);
    List<Drone> findByState(State state);
    List<Drone> findByStateAndPercentageGreaterThan(State state, Integer percentage);
    List<Drone> findByStateIn(List<State> states);
    List<Drone> findAll();
    Drone updateDrone(Drone drone);
    void deleteDroneBySerialNumber(String serialNumber);
}
