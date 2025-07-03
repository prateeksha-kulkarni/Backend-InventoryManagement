package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByName(String name);
    Optional<ProductEntity> findBySku(String sku);
}
