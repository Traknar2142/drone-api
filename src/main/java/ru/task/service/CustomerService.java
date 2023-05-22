package ru.task.service;

import ru.task.model.Customer;

public interface CustomerService {
    Customer save(Customer customer);
    Customer findByName(String name);
}
