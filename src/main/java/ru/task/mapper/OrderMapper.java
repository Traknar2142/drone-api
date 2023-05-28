package ru.task.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.task.dto.OrderDto;
import ru.task.model.Order;

@Service
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    public OrderDto toDto(Order entity){
        return modelMapper.map(entity, OrderDto.class);
    }
}
