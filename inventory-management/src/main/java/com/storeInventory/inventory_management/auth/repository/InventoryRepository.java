package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    List<InventoryEntity> findByStore_StoreId(UUID storeId);
    List<InventoryEntity> findByProduct_ProductId(UUID productId);
    Optional<InventoryEntity> findByStore_StoreIdAndProduct_ProductId(UUID storeId, UUID productId);

    @Query ("SELECT i FROM InventoryEntity i WHERE " +
            "LOWER(i.product.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(CAST(i.product.category AS string)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.product.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.product.sku) LIKE LOWER(CONCAT('%', :query, '%'))" +
            " AND i.store.storeId = :storeId")

    List<InventoryEntity> searchInventory(String query, UUID storeId);
} 