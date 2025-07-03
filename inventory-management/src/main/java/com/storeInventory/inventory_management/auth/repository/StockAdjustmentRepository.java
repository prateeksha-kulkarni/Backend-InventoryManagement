package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustmentEntity, UUID> {
} 