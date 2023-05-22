package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.model.Address;
import ru.task.model.Customer;
import ru.task.repository.AddressRepository;
import ru.task.repository.CustomerRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository repository;
    private final AddressRepository addressRepository;

    public CustomerServiceImpl(CustomerRepository repository, AddressRepository addressRepository) {
        this.repository = repository;
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        Address address = addressRepository.save(customer.getAddress());
        customer.setAddress(address);
        return repository.save(customer);
    }

    @Override
    public Customer findByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new EntityNotFoundException("Customer with name " + name + " is not found"));
    }
}
