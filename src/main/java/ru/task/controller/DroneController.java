package ru.task.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.task.dto.CargoDto;
import ru.task.dto.DroneDto;
import ru.task.dto.request.LoadRequest;
import ru.task.dto.response.DroneOperationResponse;
import ru.task.enums.State;
import ru.task.mapper.DroneMapper;
import ru.task.model.Drone;
import ru.task.service.DroneDeliverService;
import ru.task.service.DroneLoaderService;
import ru.task.service.DroneService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/drones")
@Api(value = "Drone API", description = "Operations for managing drones")
public class DroneController {
    private final DroneService droneService;
    private final DroneLoaderService droneLoaderService;
    private final DroneDeliverService droneDeliverService;
    private final DroneMapper droneMapper;

    public DroneController(DroneService droneService, DroneLoaderService droneLoaderService, DroneDeliverService droneDeliverService, DroneMapper droneMapper) {
        this.droneService = droneService;
        this.droneLoaderService = droneLoaderService;
        this.droneDeliverService = droneDeliverService;
        this.droneMapper = droneMapper;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Create drone")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created drone"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    public ResponseEntity<DroneDto> register(@Valid @RequestBody DroneDto droneDto) {
        Drone drone = droneMapper.toEntity(droneDto);
        drone = droneService.saveDrone(drone);
        DroneDto createdDroneDto = droneMapper.toDto(drone);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDroneDto);
    }

    @GetMapping("/find-available-drones-for-loading")
    @ApiOperation(value = "Get a list of drones which available for loading")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of drones"),
            @ApiResponse(code = 404, message = "No drones found")
    })
    public ResponseEntity<List<DroneDto>> getAvailableDronesForLoading(){
        List<Drone> dronesByStates = droneService.findByStateIn(Arrays.asList(State.IDLE, State.LOADING));
        List<DroneDto> droneDtos = dronesByStates.stream()
                .map(droneMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(droneDtos);
    }

    @PostMapping(value = "/load-medicine", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Load medicine into drone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully loaded drone"),
            @ApiResponse(code = 400, message = "Drone is overweight")
    })
    public ResponseEntity<DroneOperationResponse> loadMedicine(@Valid @RequestBody LoadRequest loadRequest){
        DroneOperationResponse droneOperationResponse = droneLoaderService.manualLoadProcess(loadRequest);
        return ResponseEntity.ok(droneOperationResponse);
    }

    @PostMapping(value = "/send-drone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Send drone")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Drone sent successfully"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    public ResponseEntity<DroneOperationResponse> sendDrone(@RequestParam("serialNumber") String serialNumber){
        DroneOperationResponse droneOperationResponse = droneDeliverService.deliveryProcess(serialNumber);
        return ResponseEntity.ok(droneOperationResponse);
    }

    @GetMapping("/get-cargo-by-drone-serial-number")
    @ApiOperation(value = "Retrieve list of cargo for a given serial number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved set of cargo"),
            @ApiResponse(code = 404, message = "No cargo found")
    })
    public ResponseEntity<Set<CargoDto>> getCargoByDroneSerialNumber(@RequestParam("serialNumber") String serialNumber){
        Drone drone = droneService.findBySerialNumber(serialNumber);
        DroneDto droneDto = droneMapper.toDto(drone);
        return ResponseEntity.ok(droneDto.getCargo());
    }

    @GetMapping("/get-percentage-by-drone-serial-number")
    @ApiOperation(value = "Retrieve percentage for a given serial number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved percentage info"),
            @ApiResponse(code = 404, message = "No drones found")
    })
    public ResponseEntity<DroneOperationResponse> getPercentageByDroneSerialNumber(@RequestParam("serialNumber") String serialNumber){
        Drone drone = droneService.findBySerialNumber(serialNumber);
        DroneOperationResponse droneOperationResponse = new DroneOperationResponse(serialNumber, "percentage = " + drone.getPercentage());
        return ResponseEntity.ok(droneOperationResponse);
    }

}
