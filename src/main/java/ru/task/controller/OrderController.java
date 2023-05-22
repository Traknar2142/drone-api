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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.task.dto.OrderDto;
import ru.task.dto.request.OrderRequest;
import ru.task.mapper.OrderMapper;
import ru.task.model.Order;
import ru.task.publisher.OrderMessagePublisher;
import ru.task.service.OrderService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/orders")
@Api(value = "Order API", description = "Make orders")
public class OrderController {

    private final OrderMessagePublisher orderMessagePublisher;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(OrderMessagePublisher orderMessagePublisher, OrderService orderService, OrderMapper orderMapper) {
        this.orderMessagePublisher = orderMessagePublisher;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping(value = "/make-order", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Make order")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Drone sent successfully"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    public ResponseEntity<Map<String, String>> makeOrder(@Valid @RequestBody OrderRequest orderRequest){
        orderMessagePublisher.publish(orderRequest);
        Map<String, String> massage = new HashMap<>();
        massage.put("message", "request accepted for processing");
        return ResponseEntity.ok(massage);
    }

    @GetMapping("/get-orders-by-customer-name")
    @ApiOperation(value = "Retrieve list of orders related to the customer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list of orders"),
            @ApiResponse(code = 404, message = "No orders found")
    })
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerName(@RequestParam("customerName") String customerName){
        List<Order> orders = orderService.findBuCustomerName(customerName);
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }
}
