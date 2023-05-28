package ru.task.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.dto.CustomerDto;
import ru.task.dto.request.MedicationRequest;
import ru.task.dto.request.OrderRequest;
import ru.task.enums.State;
import ru.task.model.Cargo;
import ru.task.model.Customer;
import ru.task.model.Drone;
import ru.task.model.Medication;
import ru.task.model.Order;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderProcessor {
    private final MedicationService medicationService;
    private final CargoService cargoService;
    private final DroneService droneService;
    private final DroneDeliverService droneDeliverService;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public OrderProcessor(MedicationService medicationService, CargoService cargoService, DroneService droneService, DroneDeliverService droneDeliverService, OrderService orderService, CustomerService customerService, ModelMapper modelMapper) {
        this.medicationService = medicationService;
        this.cargoService = cargoService;
        this.droneService = droneService;
        this.droneDeliverService = droneDeliverService;
        this.orderService = orderService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public void process(OrderRequest orderRequest) {
        //Конвертируем в мапу Медицина - кол-во медицины
        Map<Medication, Integer> medMap = convertToMedMap(orderRequest.getMedicationRequests());

        //Формируем грузы
        Set<Cargo> cargos = cargoService.createCargo(medMap);

        //Создаем заказ и сохраняем
        makeOrderAndSave(orderRequest.getCustomer(), cargos);


        //Загружаем дроны и отправляем
        Iterator<Cargo> iterator = cargos.iterator();
        List<Drone> idleDrones = droneService.findByStateAndPercentageGreaterThan(State.IDLE, 25);
        if (!idleDrones.isEmpty()) {
            loadDroneAndPerformDelivery(iterator, idleDrones);
        }

    }

    private void loadDroneAndPerformDelivery(Iterator<Cargo> iterator, List<Drone> drones) {
        Cargo cargo = null;
        for (Drone drone : drones) {
            drone.setCargo(new HashSet<>());
            while (iterator.hasNext()) {
                if (cargo == null) {
                    cargo = iterator.next();
                }
                if (droneIsOverweighted(drone, cargo)) {
                    performDelivery(drone);
                    break;
                } else {
                    drone.getCargo().add(cargo);
                    cargo = null;
                }
            }
            if (!drone.getCargo().isEmpty() && drone.getState().equals(State.IDLE)) {
                performDelivery(drone);
            }
        }
    }
    private boolean droneIsOverweighted(Drone drone, Cargo cargo) {
        int innerCargoWeight = 0;
        if (!drone.getCargo().isEmpty()) {
            innerCargoWeight = drone.getCargo()
                    .stream()
                    .mapToInt(Cargo::getWeight)
                    .sum();
        }
        innerCargoWeight += cargo.getWeight();
        int resultWeight = innerCargoWeight + drone.getWeight();
        return resultWeight > 500;
    }

    private Map<Medication, Integer> convertToMedMap(List<MedicationRequest> requests) {
        List<String> medCodes = requests
                .stream().map(MedicationRequest::getMedicationCode)
                .collect(Collectors.toList());

        List<Medication> medications = medicationService.findByCodeList(medCodes);

        Map<Medication, Integer> medMap = medications.stream()
                .collect(Collectors.toMap(
                        med -> med,
                        med -> requests.stream()
                                .filter(req -> req.getMedicationCode().equals(med.getCode()))
                                .findFirst()
                                .map(MedicationRequest::getQuantity)
                                .orElse(0)
                ));
        return medMap;
    }

    private void makeOrderAndSave(CustomerDto customerDto, Set<Cargo> cargos) {
        Order order = new Order();
        Customer customer = getCustomer(customerDto);

        order.setCustomer(customer);
        order.setCargos(cargos);

        orderService.save(order);
    }

    private void performDelivery(Drone drone) {
        drone.setState(State.LOADING);
        droneService.saveDrone(drone);
        System.out.println(drone);
        droneDeliverService.deliveryProcess(drone.getSerialNumber());
    }

    private Customer getCustomer(CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        try {
            Customer byName = customerService.findByName(customer.getName());
            byName.setAddress(customer.getAddress());
            return customerService.save(byName);
        } catch (EntityNotFoundException e) {
            return customerService.save(customer);
        }
    }

}
