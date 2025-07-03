package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    List<InventoryEntity> findByStore_StoreId(UUID storeId);
    Optional<InventoryEntity> findByStore_StoreIdAndProduct_ProductId(UUID storeId, UUID productId);
} 