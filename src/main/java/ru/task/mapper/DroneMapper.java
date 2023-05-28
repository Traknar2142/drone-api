package ru.task.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.task.dto.DroneDto;
import ru.task.model.Drone;

@Service
public class DroneMapper {
    private final ModelMapper modelMapper;

    public DroneMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Drone toEntity(DroneDto dto) {
        return modelMapper.map(dto, Drone.class);
    }

    public DroneDto toDto(Drone entity) {
        return modelMapper.map(entity, DroneDto.class);
    }
}
