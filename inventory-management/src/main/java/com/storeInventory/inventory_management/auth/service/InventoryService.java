package com.storeInventory.inventory_management.auth.service;


import com.storeInventory.inventory_management.auth.model.*;
import com.storeInventory.inventory_management.auth.dto.InventoryResponseDto;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StockAdjustmentRepository stockAdjustmentRepository;

    public List<InventoryEntity> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public List<InventoryEntity> getInventoryByStore(UUID storeId) {
        return inventoryRepository.findByStore_StoreId(storeId);
    }

    public Optional<InventoryEntity> getInventoryByStoreAndProduct(UUID storeId, UUID productId) {
        return inventoryRepository.findByStore_StoreIdAndProduct_ProductId(storeId, productId);
    }

    public List<InventoryResponseDto> searchInventory(String query, UUID storeId) {
        List<InventoryEntity> inventoryEntities = inventoryRepository.searchInventory(query, storeId);
        return inventoryEntities.stream()
            .map(InventoryResponseDto::fromEntity)
            .toList();
    }

    public List<InventoryEntity> getInventoryByProduct(UUID productId) {
        return inventoryRepository.findByProduct_ProductId(productId);
    }

    public InventoryEntity createInventory(InventoryEntity inventory) {
        System.out.println("🚨 Incoming Inventory Request: " + inventory);

        if (inventory.getProduct() == null || inventory.getProduct().getProductId() == null) {
            throw new IllegalArgumentException("❌ Product ID is missing in the request");
        }

        if (inventory.getStore() == null || inventory.getStore().getStoreId() == null) {
            throw new IllegalArgumentException("❌ Store ID is missing in the request");
        }

        UUID productId = inventory.getProduct().getProductId();
        UUID storeId = inventory.getStore().getStoreId();

        System.out.println("✅ Product ID: " + productId);
        System.out.println("✅ Store ID: " + storeId);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        StoreEntity store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found: " + storeId));

        inventory.setProduct(product);
        inventory.setStore(store);

        return inventoryRepository.save(inventory);
    }


    public InventoryEntity adjustQuantity(String productName, int quantity, ChangeType type, UUID storeId, UUID userId, String reason) {
        // 1. Fetch product
        ProductEntity product = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));
        UUID productId = product.getProductId();

        // 2. Fetch inventory
        InventoryEntity inventory = inventoryRepository.findByStore_StoreIdAndProduct_ProductId(storeId, productId)
                .orElseThrow(() -> new RuntimeException("Inventory for this product does not exist at the given store. Please add the product first with a minThreshold."));

        // 3. Adjust quantity
        if (type == ChangeType.ADD) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else if (type == ChangeType.REMOVE) {
            if (inventory.getQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock to remove.");
            }
            inventory.setQuantity(inventory.getQuantity() - quantity);
        } else {
            throw new RuntimeException("Invalid adjustment type.");
        }

        InventoryEntity updatedInventory = inventoryRepository.save(inventory);


        // 4. Fetch user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        System.out.println("👤 User ID: " + user.getUserId());

        // 5. Log stock adjustment
        StockAdjustmentEntity adjustment = new StockAdjustmentEntity();
        adjustment.setInventory(updatedInventory);
        adjustment.setUser(user);
        adjustment.setChangeType(type);
        adjustment.setQuantityChange(quantity);
        adjustment.setReason(reason);


        stockAdjustmentRepository.save(adjustment);

        return updatedInventory;
    }

} 