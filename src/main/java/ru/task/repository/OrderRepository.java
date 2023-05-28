package ru.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.task.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerName(String name);
}
