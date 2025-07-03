package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public List<InventoryEntity> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public List<InventoryEntity> getInventoryByStore(UUID storeId) {
        return inventoryRepository.findByStore_StoreId(storeId);
    }

    public Optional<InventoryEntity> getInventoryByStoreAndProduct(UUID storeId, UUID productId) {
        return inventoryRepository.findByStore_StoreIdAndProduct_ProductId(storeId, productId);
    }

    public InventoryEntity createInventory(InventoryEntity inventory) {
        return inventoryRepository.save(inventory);
    }
} 