package org.rubisemi.micro.order.service;

import feign.FeignException;
import org.rubisemi.micro.common.exception.ItemNotFoundException;
import org.rubisemi.micro.common.exception.SysErrorException;
import org.rubisemi.micro.invertory.entity.InventoryEntity;
import org.rubisemi.micro.invertory.entity.ReserveDetail;
import org.rubisemi.micro.invertory.entity.ReserveRequest;
import org.rubisemi.micro.invertory.entity.ReserveResult;
import org.rubisemi.micro.order.client.InventoryClient;
import org.rubisemi.micro.order.entity.Order;
import org.rubisemi.micro.order.entity.OrderDetail;
import org.rubisemi.micro.order.entity.OrderReqDetail;
import org.rubisemi.micro.order.entity.OrderRequest;
import org.rubisemi.micro.order.exception.InventoryShortageException;
import org.rubisemi.micro.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    InventoryClient inventoryClient;

    @Autowired
    OrderRepository orderRepository;

    public Optional<Order> getOrderById(long orderId){
        return this.orderRepository.findById(orderId);
    }

    public List<Order> queryOrder(){
        return this.orderRepository.query();
    }

    public Order placeOrder(OrderRequest request) throws InventoryShortageException, SysErrorException, ItemNotFoundException {
        // Check the inventory at first
        this.checkAndReserve(request);
        // Place the order
        try {
            return this.doPlaceOrder(request);
        }catch(Exception e){
            this.rollbackReserve(request);
            throw new SysErrorException("System error occurred");
        }

    }

    public Order doPlaceOrder(OrderRequest request){
        Order newOrder = this.convert(request);
        Order placedOrder = this.orderRepository.save(newOrder);
        this.settleReserve(request);
        return placedOrder;
    }

    private void checkAndReserve(OrderRequest request) throws InventoryShortageException {
        ReserveRequest reserveRequest = this.to(request);
        ReserveResult result = this.inventoryClient.checkAndReserve(reserveRequest);
        if(result.getCode() == 406){
            throw new InventoryShortageException(result.getMessage());
        }
    }

    private void settleReserve(OrderRequest request){
        ReserveRequest reserveRequest = this.to(request);
        this.inventoryClient.settleReserve(reserveRequest);
    }

    private void rollbackReserve(OrderRequest request){
        ReserveRequest reserveRequest = this.to(request);
        this.inventoryClient.rollbackReserve(reserveRequest);
    }

    private ReserveRequest to(OrderRequest req){
        ReserveRequest request = new ReserveRequest();
        request.setDetails(new ArrayList<>());
        for(OrderReqDetail detail : req.getDetails()){
            ReserveDetail item = new ReserveDetail();
            item.setProductId(detail.getProductId());
            item.setReserveAmount(detail.getAmount());
            request.getDetails().add(item);
        }
        return request;
    }

    private InventoryEntity getProductInventory(long productId) throws ItemNotFoundException, SysErrorException {
        try{
            InventoryEntity resp = inventoryClient.getProductInventory(productId);
            return resp;
        }catch(FeignException ex){
            if(ex.status() == HttpStatus.NOT_FOUND.value()){
                throw new ItemNotFoundException(ex.getMessage());
            }else{
                throw new SysErrorException(ex.getMessage());
            }
        }
    }

    private Order convert(OrderRequest request){
        Order order = new Order();
        order.setDetails(new ArrayList<>());
        double amount = 0D;
        double totalPrice = 0D;
        for(OrderReqDetail reqDetail : request.getDetails()){
            OrderDetail detail = new OrderDetail();
            detail.setProductId(reqDetail.getProductId());
            detail.setAmount(reqDetail.getAmount());
            order.getDetails().add(detail);
            amount += detail.getAmount();
        }
        order.setAmount(amount);
        order.setTotalPrice(totalPrice);
        order.setCreatedAt(new Date());
        return order;
    }


}
