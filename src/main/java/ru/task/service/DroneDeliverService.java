package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.dto.response.DroneOperationResponse;
import ru.task.enums.State;
import ru.task.exception.DroneStateException;
import ru.task.model.Drone;

@Service
public class DroneDeliverService {
    private final DroneService droneService;

    public DroneDeliverService(DroneService droneService) {
        this.droneService = droneService;
    }

    @Transactional
    public DroneOperationResponse deliveryProcess(String droneSerialNumber){
        Drone drone = droneService.findBySerialNumber(droneSerialNumber);
        checkState(drone);
        drone.setState(State.DELIVERING);
        droneService.saveDrone(drone);
        return new DroneOperationResponse(droneSerialNumber,"Drone sent successfully");
    }

    private void checkState(Drone drone){
        if (!drone.getState().equals(State.LOADING)){
            throw new DroneStateException("The drone must be in the LOADING state to start the delivery process. Current state: "+ drone.getState());
        }
    }
}
