package ru.task.service;

import ru.task.model.Order;

import java.util.List;

public interface OrderService {
    void save(Order order);
    List<Order> findBuCustomerName(String name);
}
