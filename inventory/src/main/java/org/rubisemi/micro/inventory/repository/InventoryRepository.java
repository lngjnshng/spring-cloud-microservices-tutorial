package org.rubisemi.micro.inventory.repository;

import org.rubisemi.micro.invertory.entity.InventoryEntity;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InventoryRepository {

    List<InventoryEntity> queryAll();

    Optional<InventoryEntity> findByProductId(Long productId);

    List<InventoryEntity> queryByProductIds(Collection<Long> productIds);
}
