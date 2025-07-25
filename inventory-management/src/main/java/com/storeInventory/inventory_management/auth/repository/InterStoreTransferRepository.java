package com.storeInventory.inventory_management.auth.repository;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InterStoreTransferRepository extends JpaRepository<InterStoreTransferEntity, UUID> {
    List<InterStoreTransferEntity> findByToStore_StoreIdAndStatus(UUID toStoreId, TransferStatus status);
    List<InterStoreTransferEntity> findByFromStore_StoreId(UUID fromStoreId);
    List<InterStoreTransferEntity> findByToStore_StoreId(UUID toStoreId);

    List<InterStoreTransferEntity> findByStatusAndTimestampBetween(
            TransferStatus status, LocalDateTime start, LocalDateTime end);


    List<InterStoreTransferEntity> findByFromStore_StoreIdOrToStore_StoreId(UUID fromStoreId, UUID toStoreId);

    @Query("""
        SELECT t FROM InterStoreTransferEntity t
        JOIN FETCH t.product
        JOIN FETCH t.fromStore
        JOIN FETCH t.toStore
        JOIN FETCH t.requestedBy
        LEFT JOIN FETCH t.approvedBy
        WHERE t.toStore.storeId = :storeId AND t.status = :status
    """)
    List<InterStoreTransferEntity> fetchTransfersToStoreWithDetails(UUID storeId, TransferStatus status);

    @Query("""
        SELECT t FROM InterStoreTransferEntity t
        JOIN FETCH t.product
        JOIN FETCH t.fromStore
        JOIN FETCH t.toStore
        JOIN FETCH t.requestedBy
        LEFT JOIN FETCH t.approvedBy
        WHERE t.fromStore.storeId = :storeId OR t.toStore.storeId = :storeId
    """)
    List<InterStoreTransferEntity> fetchAllTransfersWithDetails(UUID storeId);

    @Query("SELECT t FROM InterStoreTransferEntity t " +
       "JOIN FETCH t.product " +
       "JOIN FETCH t.fromStore " +
       "JOIN FETCH t.toStore " +
       "JOIN FETCH t.requestedBy " +
       "LEFT JOIN FETCH t.approvedBy")
    List<InterStoreTransferEntity> findAll();

}

