package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class StockAdjustmentRepositoryTest {

    @Autowired
    StockAdjustmentRepository stockAdjustmentRepository;
    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void whenSavedStockAdjustment_thenReturnNonNullAdjustment() {
        StoreEntity store = new StoreEntity();
        store.setName("Test Store");
        store.setLocation("Test Location");
        store = storeRepository.save(store);

        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setSku("SKU123");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setDescription("A test product");
        product = productRepository.save(product);

        InventoryEntity inventory = new InventoryEntity();
        inventory.setStore(store);
        inventory.setProduct(product);
        inventory.setQuantity(10);
        inventory = inventoryRepository.save(inventory);

        UserEntity user = UserEntity.builder()
                .username("user1")
                .password("password")
                .email("user1@example.com")
                .role(UserRole.MANAGER)
                .name("User One")
                .store(store)
                .build();
        user = userRepository.save(user);

        StockAdjustmentEntity adjustment = new StockAdjustmentEntity();
        adjustment.setInventory(inventory);
        adjustment.setUser(user);
        adjustment.setChangeType(ChangeType.ADD);
        adjustment.setQuantityChange(5);
        adjustment.setReason("Initial stock");

        StockAdjustmentEntity savedAdjustment = stockAdjustmentRepository.save(adjustment);

        Assertions.assertNotNull(savedAdjustment);
        Assertions.assertEquals(5, savedAdjustment.getQuantityChange());
        Assertions.assertEquals(ChangeType.ADD, savedAdjustment.getChangeType());
        Assertions.assertEquals(inventory.getInventoryId(), savedAdjustment.getInventory().getInventoryId());
        Assertions.assertEquals(user.getUserId(), savedAdjustment.getUser().getUserId());
    }
} 