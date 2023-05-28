package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.dto.DroneMainParameters;
import ru.task.dto.request.LoadRequest;
import ru.task.dto.request.MedicationRequest;
import ru.task.model.Drone;
import ru.task.model.Medication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DroneMainParameterMapper {
    private final MedicationService medicationService;
    private final DroneService droneService;

    public DroneMainParameterMapper(MedicationService medicationService, DroneService droneService) {
        this.medicationService = medicationService;
        this.droneService = droneService;
    }

    @Transactional(readOnly = true)
    public DroneMainParameters getDroneMainParameters(LoadRequest loadRequest) {
        DroneMainParameters droneMainParameters = new DroneMainParameters();
        Drone drone = droneService.findBySerialNumber(loadRequest.getDroneSerialNumber());
        List<String> medCodes = new ArrayList<>();
        loadRequest.getMedicationRequests().forEach(req -> medCodes.add(req.getMedicationCode()));

        List<Medication> medicationList = medicationService.findByCodeList(medCodes);
        Map<Medication, Integer> medMap = medicationList.stream()
                .collect(Collectors.toMap(
                        med -> med,
                        med -> loadRequest.getMedicationRequests().stream()
                                .filter(req -> req.getMedicationCode().equals(med.getCode()))
                                .findFirst()
                                .map(MedicationRequest::getQuantity)
                                .orElse(0)
                ));

        int medWeight = medMap.entrySet()
                .stream()
                .mapToInt(el -> el.getKey().getWeight() * el.getValue())
                .sum();

        droneMainParameters.setDroneSerialNumber(loadRequest.getDroneSerialNumber());
        droneMainParameters.setWeight(drone.getWeight() + medWeight);
        droneMainParameters.setPercentage(drone.getPercentage());
        droneMainParameters.setState(drone.getState());
        droneMainParameters.setMedications(medMap);

        return droneMainParameters;
    }
}
