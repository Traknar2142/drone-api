package ru.task.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.task.model.Order;
import ru.task.repository.OrderRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.repository = orderRepository;
    }

    @Override
    @Transactional
    public void save(Order order) {
        repository.save(order);
    }

    @Override
    @Transactional
    public List<Order> findBuCustomerName(String name) {
        List<Order> orders = repository.findByCustomerName(name);
        if (orders.isEmpty()){
            throw new EntityNotFoundException("Orders are not found by given customer's name: " + name);
        } else return repository.findByCustomerName(name);
    }
}
