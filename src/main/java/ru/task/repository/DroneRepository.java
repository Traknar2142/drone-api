package ru.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.task.enums.State;
import ru.task.model.Drone;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Optional<Drone> findBySerialNumber(String serialNumber);
    List<Drone> findByState(State state);
    List<Drone> findByStateAndPercentageGreaterThan(State state, int percentage);
    List<Drone> findByStateIn(List<State> states);
    void deleteBySerialNumber(String serialNumber);
}
