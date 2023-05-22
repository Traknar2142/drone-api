package ru.task.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.task.model.Drone;
import ru.task.service.DroneService;

import java.util.List;

/*@Component
@Slf4j
public class ChargeCheckScheduler {
    private final DroneService droneService;

    public ChargeCheckScheduler(DroneService droneService) {
        this.droneService = droneService;
    }

    @Scheduled(fixedRate = 10000)
    public void chargeCheck(){
        List<Drone> drones = droneService.findAll();
        drones.forEach(dr -> log.info("Drone charge check: serial number: " + dr.getSerialNumber() + " percentage: " + dr.getPercentage()));
    }
}*/
