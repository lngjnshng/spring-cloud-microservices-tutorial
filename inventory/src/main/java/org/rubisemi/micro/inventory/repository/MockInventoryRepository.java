package org.rubisemi.micro.inventory.repository;

import org.rubisemi.micro.invertory.entity.InventoryEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MockInventoryRepository implements InventoryRepository{

    private static List<InventoryEntity> inventories;

    static{
        init();
    }

    @Override
    public List<InventoryEntity> queryAll() {
        return inventories;
    }

    @Override
    public Optional<InventoryEntity> findByProductId(Long productId) {
        return inventories.stream().filter(entity -> entity.getProductId().longValue() == productId.longValue()).findFirst();
    }

    @Override
    public List<InventoryEntity> queryByProductIds(Collection<Long> productIds) {
        return inventories.stream().filter(entity -> productIds.contains(entity.getProductId())).toList();
    }

    private static void init(){
        if(inventories == null){
            inventories = new ArrayList<>();
            for(long i = 1; i <= 5; i++){
                InventoryEntity entity = new InventoryEntity();
                entity.setProductId(i);
                entity.setProductName(String.format("PRODUCT_0%s", i));
                entity.setReserved(0D);
                entity.setAmount(i * 10D);
                entity.setPrice(10D + i);
                entity.setCreatedAt(new Date());
                inventories.add(entity);
            }
        }
    }
}
