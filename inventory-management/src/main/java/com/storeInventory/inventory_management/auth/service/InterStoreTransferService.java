package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterStoreTransferService {
    private final InterStoreTransferRepository transferRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public List<InterStoreTransferEntity> getTransfersToStore(UUID toStoreId, TransferStatus status) {
        return transferRepository.findByToStore_StoreIdAndStatus(toStoreId, status);
    }

    public List<InterStoreTransferEntity> getTransfersFromStore(UUID fromStoreId) {
        return transferRepository.findByFromStore_StoreId(fromStoreId);
    }

    public InterStoreTransferEntity createTransfer(InterStoreTransferEntity transfer) {
        // Look up requestedBy user by username
        if (transfer.getRequestedBy() != null && transfer.getRequestedBy().getUsername() != null) {
            UserEntity user = userRepository.findByUsername(transfer.getRequestedBy().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + transfer.getRequestedBy().getUsername()));
            transfer.setRequestedBy(user);
        }
        // Look up approvedBy user by username (if present)
        if (transfer.getApprovedBy() != null && transfer.getApprovedBy().getUsername() != null) {
            UserEntity user = userRepository.findByUsername(transfer.getApprovedBy().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + transfer.getApprovedBy().getUsername()));
            transfer.setApprovedBy(user);
        }
        return transferRepository.save(transfer);
    }

    public InterStoreTransferEntity updateTransfer(InterStoreTransferEntity transfer) {
        // Fail fast if required fields are missing
        if (transfer.getTransferId() == null) {
            throw new RuntimeException("transferId is required");
        }
        // Always load the transfer from the DB
        InterStoreTransferEntity dbTransfer = transferRepository.findById(transfer.getTransferId())
            .orElseThrow(() -> new RuntimeException("Transfer not found"));
        // Only update allowed fields
        dbTransfer.setStatus(transfer.getStatus());
        // Look up approvedBy user by username (if present)
        if (transfer.getApprovedBy() != null && transfer.getApprovedBy().getUsername() != null) {
            UserEntity user = userRepository.findByUsername(transfer.getApprovedBy().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + transfer.getApprovedBy().getUsername()));
            dbTransfer.setApprovedBy(user);
        }
        // Rehydrate fromStore, toStore, and product from DB
        if (dbTransfer.getFromStore() != null && dbTransfer.getFromStore().getStoreId() != null) {
            StoreEntity fromStore = storeRepository.findById(dbTransfer.getFromStore().getStoreId())
                .orElseThrow(() -> new RuntimeException("From store not found"));
            dbTransfer.setFromStore(fromStore);
        }
        if (dbTransfer.getToStore() != null && dbTransfer.getToStore().getStoreId() != null) {
            StoreEntity toStore = storeRepository.findById(dbTransfer.getToStore().getStoreId())
                .orElseThrow(() -> new RuntimeException("To store not found"));
            dbTransfer.setToStore(toStore);
        }
        if (dbTransfer.getProduct() != null && dbTransfer.getProduct().getProductId() != null) {
            ProductEntity product = productRepository.findById(dbTransfer.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            dbTransfer.setProduct(product);
        }
        // Look up requestedBy user by username
        if (dbTransfer.getRequestedBy() != null && dbTransfer.getRequestedBy().getUsername() != null) {
            UserEntity user = userRepository.findByUsername(dbTransfer.getRequestedBy().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + dbTransfer.getRequestedBy().getUsername()));
            dbTransfer.setRequestedBy(user);
        }
        // If approving the transfer, update inventories
        if (dbTransfer.getStatus() == TransferStatus.APPROVED || dbTransfer.getStatus() == TransferStatus.COMPLETED) {
            UUID fromStoreId = dbTransfer.getFromStore().getStoreId();
            UUID toStoreId = dbTransfer.getToStore().getStoreId();
            UUID productId = dbTransfer.getProduct().getProductId();
            int qty = dbTransfer.getQuantity();

            // Decrease inventory in source store
            InventoryEntity fromInv = inventoryRepository.findByStore_StoreIdAndProduct_ProductId(fromStoreId, productId)
                .orElseThrow(() -> new RuntimeException("Source inventory not found"));
            if (fromInv.getQuantity() < qty) {
                throw new RuntimeException("Insufficient stock in source store");
            }
            fromInv.setQuantity(fromInv.getQuantity() - qty);
            inventoryRepository.save(fromInv);

            // Increase inventory in destination store (create if not exists)
            InventoryEntity toInv = inventoryRepository.findByStore_StoreIdAndProduct_ProductId(toStoreId, productId)
                .orElse(null);
            if (toInv == null) {
                toInv = new InventoryEntity();
                toInv.setStore(dbTransfer.getToStore());
                toInv.setProduct(dbTransfer.getProduct());
                toInv.setQuantity(qty);
                toInv.setMinThreshold(0);
            } else {
                toInv.setQuantity(toInv.getQuantity() + qty);
            }
            inventoryRepository.save(toInv);
        }
        return transferRepository.save(dbTransfer);
    }

    public InterStoreTransferEntity getTransferById(UUID transferId) {
        return transferRepository.findById(transferId)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));
    }
} 