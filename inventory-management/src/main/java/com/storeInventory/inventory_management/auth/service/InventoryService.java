package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
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

    public InventoryEntity adjustQuantity(String productName, int quantity, String type, UUID storeId) {
        // 1. Fetch product by name
        ProductEntity product = productRepository.findByNameIgnoreCase(productName)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));

        UUID productId = product.getProductId();

        // 2. Fetch inventory entry for store and product
        Optional<InventoryEntity> optionalInventory = inventoryRepository.findByStore_StoreIdAndProduct_ProductId(storeId, productId);

        if (optionalInventory.isEmpty()) {
            throw new RuntimeException("Inventory for this product does not exist at the given store. Please add the product first with a minThreshold.");
        }

        InventoryEntity inventory = optionalInventory.get();

        // 3. Adjust quantity
        if (type.equalsIgnoreCase("add")) {
            inventory.setQuantity(inventory.getQuantity() + quantity);
        } else if (type.equalsIgnoreCase("remove")) {
            if (inventory.getQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock to remove.");
            }
            inventory.setQuantity(inventory.getQuantity() - quantity);
        } else {
            throw new RuntimeException("Invalid adjustment type. Use 'add' or 'remove'.");
        }

        return inventoryRepository.save(inventory);
    }


} 