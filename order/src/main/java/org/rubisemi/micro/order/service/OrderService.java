package org.rubisemi.micro.order.service;

import org.rubisemi.micro.order.entity.OrderEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OrderService {

    public Optional<OrderEntity> getOrderById(long orderId){
        if(orderId == 1 || orderId == 2){
            return Optional.of(mock(orderId));
        }else{
            return Optional.empty();
        }
    }

    private OrderEntity mock(long id){
        OrderEntity entity = new OrderEntity();
        entity.setId(id);
        entity.setAmount(1D);
        entity.setTotalPrice(102D);
        entity.setOwner(id == 1 ? "john" : "rubisemi");
        entity.setCreatedAt(new Date());
        return entity;
    }
}
