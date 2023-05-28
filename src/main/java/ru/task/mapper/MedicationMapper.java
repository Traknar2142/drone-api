package ru.task.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.task.dto.MedicationDto;
import ru.task.model.Medication;

@Service
public class MedicationMapper {
    private final ModelMapper modelMapper;

    public MedicationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Medication toEntity(MedicationDto dto){
        return modelMapper.map(dto, Medication.class);
    }

    public MedicationDto toDto(Medication entity){
        return modelMapper.map(entity, MedicationDto.class);
    }
}
