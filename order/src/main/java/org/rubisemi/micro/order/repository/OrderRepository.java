package org.rubisemi.micro.order.repository;

import org.rubisemi.micro.order.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository {

    List<Order> query();

    Optional<Order> findById(Long id);

    public Order save(Order order);

}
