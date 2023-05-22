package ru.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.task.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
