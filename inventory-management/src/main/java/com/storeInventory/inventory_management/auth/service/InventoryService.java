package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
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

    public List<InventoryEntity> searchInventory(String query, UUID storeId) {
        return inventoryRepository.searchInventory(query,storeId);
    }

    public InventoryEntity createInventory(InventoryEntity inventory) {
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