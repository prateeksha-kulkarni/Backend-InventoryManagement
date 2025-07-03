package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InterStoreTransferRepository extends JpaRepository<InterStoreTransferEntity, UUID> {
    List<InterStoreTransferEntity> findByToStore_StoreIdAndStatus(UUID toStoreId, TransferStatus status);
    List<InterStoreTransferEntity> findByFromStore_StoreId(UUID fromStoreId);
    List<InterStoreTransferEntity> findByToStore_StoreId(UUID toStoreId);
} 