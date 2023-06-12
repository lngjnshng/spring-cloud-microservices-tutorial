package org.rubisemi.micro.order.repository;

import org.rubisemi.micro.order.entity.Order;
import org.rubisemi.micro.order.entity.OrderDetail;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class MockOrderRepository implements OrderRepository{

    private static List<Order> orders;

    static {
        init();
    }

    @Override
    public List<Order> query() {
        return orders;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orders.stream().filter(order -> order.getId() == id).findFirst();
    }

    @Override
    public Order save(Order order){
        // Get the max order id
        Optional<Order> opt = orders.stream().max( (order1, order2) -> order1.getId() > order2.getId() ? 0 : 1);
        Long id = opt.isEmpty() ? 1 : opt.get().getId() + 1;
        order.setId(id);
        orders.add(order);
        return order;
    }

    private static void init(){
        orders = new ArrayList<>();
        for(long i = 1; i <= 2; i++){
            Order order = new Order();
            order.setId(i);
            order.setOwner(i == 1 ? "john" : "smith");
            order.setTotalPrice(i * 10D);
            order.setAmount(1D);
            order.setCreatedAt(new Date());
            List<OrderDetail> details = new ArrayList<>();
            OrderDetail detail = new OrderDetail();
            detail.setProductId(i);
            detail.setPrice(i * 10D);
            detail.setAmount(1D);
            details.add(detail);
            order.setDetails(details);
            orders.add(order);
        }
    }
}
