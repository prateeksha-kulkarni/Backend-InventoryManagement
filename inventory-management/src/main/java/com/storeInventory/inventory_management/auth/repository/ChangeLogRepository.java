package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLogEntity, UUID> {
} 