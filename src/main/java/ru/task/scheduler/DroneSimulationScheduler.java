package ru.task.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.task.enums.State;
import ru.task.model.Drone;
import ru.task.service.CargoService;
import ru.task.service.DroneService;

import java.util.Collections;
import java.util.List;

@Component
public class DroneSimulationScheduler {
    private final DroneService droneService;
    private final CargoService cargoService;

    public DroneSimulationScheduler(DroneService droneService, CargoService cargoService) {
        this.droneService = droneService;
        this.cargoService = cargoService;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void moveToDeliveredState() {
        moveState(State.DELIVERING, State.DELIVERED, 8);
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void moveToReturningState() {
        moveState(State.DELIVERED, State.RETURNING, 9);
    }

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void moveToIdleState() {
        moveState(State.RETURNING, State.IDLE, 8);
    }

    @Scheduled(fixedRate = 7000)
    public void chargeStage() {
        List<Drone> drones = droneService.findByState(State.IDLE);
        Integer maxCharge = 100;
        Integer chargeStep = 5;
        if (!drones.isEmpty()){
            for (Drone drone : drones) {
                drone.setPercentage(drone.getPercentage() + chargeStep);
                if (drone.getPercentage().compareTo(maxCharge) > 0){
                    drone.setPercentage(maxCharge);
                }
            }
            droneService.saveAll(drones);
        }
    }


    private void moveState(State current, State target, Integer chargeConsumption) {
        List<Drone> dronesInDelivering = droneService.findByState(current);
        if (!dronesInDelivering.isEmpty()) {
            for (Drone drone : dronesInDelivering) {
                drone.setState(target);
                drone.setPercentage(drone.getPercentage() - chargeConsumption);
                if (target.equals(State.DELIVERED)){
                    cargoService.deleteCargo(drone.getCargo());
                    drone.setCargo(Collections.emptySet());
                }
            }
            droneService.saveAll(dronesInDelivering);
        }
    }
}
