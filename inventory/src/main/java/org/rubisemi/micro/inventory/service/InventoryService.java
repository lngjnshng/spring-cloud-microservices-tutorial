package org.rubisemi.micro.inventory.service;

import org.rubisemi.micro.inventory.repository.InventoryRepository;
import org.rubisemi.micro.invertory.entity.InventoryEntity;
import org.rubisemi.micro.invertory.entity.ReserveDetail;
import org.rubisemi.micro.invertory.entity.ReserveRequest;
import org.rubisemi.micro.invertory.entity.ReserveResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public Optional<InventoryEntity> getProductInventoryById(long productId){
        return inventoryRepository.findByProductId(productId);
    }

    public List<InventoryEntity> query(){
        return inventoryRepository.queryAll();
    }

    public ReserveResult checkAndReserve(ReserveRequest request){
        List<Long> productIds = request.getDetails().stream().map(item -> item.getProductId()).toList();
        List<InventoryEntity> matched = this.inventoryRepository.queryByProductIds(productIds);
        Map<Long, InventoryEntity> map = this.listToMap(matched);
        int code = ReserveResult.SUCCESS;
        String message = null;
        Map<Long, Double> reserveMap = new HashMap<>();
        for(ReserveDetail detail : request.getDetails()){
            InventoryEntity entity = map.get(detail.getProductId());
            if(entity == null){
                code = ReserveResult.NOT_FOUND;
                message = String.format("Product [ %s ] not found", entity.getProductName());
                break;
            }
            if(entity.getAmount().doubleValue() < detail.getReserveAmount().doubleValue()){
                code = ReserveResult.NOT_SUFFICIENT;
                message = String.format("Product [ %s ] not sufficient", entity.getProductName());
                break;
            }
            reserveMap.put(detail.getProductId(), detail.getReserveAmount());
        }
        if(code != ReserveResult.SUCCESS){
            return new ReserveResult(code, message);
        }
        // Update the reserve amount
        for(InventoryEntity entity : matched){
            Double reserveAmount = reserveMap.get(entity.getProductId());
            entity.setAmount(entity.getAmount() - reserveAmount);
            entity.setReserved(entity.getReserved() + reserveAmount);
        }
        return new ReserveResult(code, message);
    }

    private Map<Long, InventoryEntity> listToMap(List<InventoryEntity> list){
        Map<Long, InventoryEntity> result = new HashMap<>();
        for(InventoryEntity item : list){
            result.put(item.getProductId(), item);
        }
        return result;
    }

    public Boolean settleReserve(ReserveRequest request){
        List<Long> productIds = request.getDetails().stream().map(item -> item.getProductId()).toList();
        List<InventoryEntity> matched = this.inventoryRepository.queryByProductIds(productIds);
        for(ReserveDetail detail : request.getDetails()){
            Map<Long, InventoryEntity> map = this.listToMap(matched);
            InventoryEntity entity = map.get(detail.getProductId());
            if(entity != null){
                entity.setReserved(entity.getReserved() - detail.getReserveAmount());
            }
        }
        return true;
    }

    public Boolean releaseReserve(ReserveRequest request){
        List<Long> productIds = request.getDetails().stream().map(item -> item.getProductId()).toList();
        List<InventoryEntity> matched = this.inventoryRepository.queryByProductIds(productIds);
        for(ReserveDetail detail : request.getDetails()){
            Map<Long, InventoryEntity> map = this.listToMap(matched);
            InventoryEntity entity = map.get(detail.getProductId());
            if(entity != null){
                entity.setReserved(entity.getReserved() - detail.getReserveAmount());
                entity.setAmount(entity.getAmount() + detail.getReserveAmount());
            }
        }
        return true;
    }


}
