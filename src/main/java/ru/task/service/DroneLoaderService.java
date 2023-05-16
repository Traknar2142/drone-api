package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.dto.DroneMainParameters;
import ru.task.dto.request.LoadRequest;
import ru.task.dto.response.DroneOperationResponse;
import ru.task.enums.State;
import ru.task.model.Cargo;
import ru.task.model.Drone;
import ru.task.model.Medication;
import ru.task.validator.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DroneLoaderService {
    private final DroneMainParameterMapper mapper;
    private final List<Validator> validators;
    private final DroneService droneService;
    private final CargoService cargoService;

    public DroneLoaderService(DroneMainParameterMapper mapper, List<Validator> validators, DroneService droneService, CargoService cargoService) {
        this.mapper = mapper;
        this.validators = validators;
        this.droneService = droneService;
        this.cargoService = cargoService;
    }

    @Transactional
    public DroneOperationResponse loadProcess(LoadRequest loadRequest) {
        DroneMainParameters droneMainParameters = mapper.getDroneMainParameters(loadRequest);
        //validation stage
        validators.forEach(validator -> validator.validate(droneMainParameters));

        Drone drone = droneService.findBySerialNumber(droneMainParameters.getDroneSerialNumber());

        //actualization stage
        actualizeState(drone);

        //load stage
        List<Medication> medications = droneMainParameters.getMedications();
        Set<Cargo> cargos = new HashSet<>();
        for (Medication medication : medications) {
            Integer quantity = loadRequest.getMedicationRequests()
                    .stream()
                    .filter(req -> req.getMedicationCode().equals(medication.getCode()))
                    .findFirst()
                    .get().getQuantity();
            Integer cargoWeight = medication.getWeight() * quantity;
            cargos.add(new Cargo(quantity, cargoWeight, medication));
        }

        if (!drone.getCargo().isEmpty()){
            cargoService.deleteCargo(drone.getCargo());
        }

        drone.setCargo(cargos);
        droneService.saveDrone(drone);
        return new DroneOperationResponse(droneMainParameters.getDroneSerialNumber(), "Drone loaded successfully");
    }

    private void actualizeState(Drone drone) {
        if (drone.getState().equals(State.IDLE)) {
            drone.setState(State.LOADING);
        }
    }

}
