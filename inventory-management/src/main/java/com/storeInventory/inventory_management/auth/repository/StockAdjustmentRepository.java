package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.UUID;

@Repository
public interface StockAdjustmentRepository extends JpaRepository<StockAdjustmentEntity, UUID> {

    @Query("SELECT sa FROM StockAdjustmentEntity sa " +
           "JOIN FETCH sa.user " +
           "JOIN FETCH sa.inventory i " +
           "JOIN FETCH i.store " +
           "JOIN FETCH i.product")
    List<StockAdjustmentEntity> findAllWithRelations();
} 