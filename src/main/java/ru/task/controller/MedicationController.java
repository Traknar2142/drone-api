package ru.task.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.task.dto.MedicationDto;
import ru.task.mapper.MedicationMapper;
import ru.task.model.Medication;
import ru.task.service.MedicationService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/medication")
@Api(value = "Medication API", description = "Operations for medications")
public class MedicationController {
    private final MedicationService medicationService;
    private final MedicationMapper mapper;

    public MedicationController(MedicationService medicationService, MedicationMapper mapper) {
        this.medicationService = medicationService;
        this.mapper = mapper;
    }

    @GetMapping("/find-all")
    @ApiOperation(value = "Get a list of all medicine")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of medication"),
            @ApiResponse(code = 404, message = "No medication found")
    })
    public ResponseEntity<List<MedicationDto>> getAllMedication(){
        List<Medication> medications = medicationService.findAll();
        List<MedicationDto> medicationDtos= medications.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(medicationDtos);
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Add medication")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added medication"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    public ResponseEntity<MedicationDto> addMedication(@Valid @RequestBody MedicationDto medicationDto){
        Medication medication = mapper.toEntity(medicationDto);
        medication = medicationService.saveMedication(medication);
        MedicationDto createdMedication = mapper.toDto(medication);
        return ResponseEntity.ok(createdMedication);
    }
}
