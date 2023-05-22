package ru.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.task.model.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByName(String name);
}
