package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.model.Address;
import ru.task.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService{
    private final AddressRepository repository;

    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Address save(Address address) {
        return repository.save(address);
    }
}
