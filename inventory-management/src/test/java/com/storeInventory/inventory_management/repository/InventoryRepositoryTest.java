package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class InventoryRepositoryTest {

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    public void whenSavedInventory_thenReturnNonNullInventory() {
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

        InventoryEntity savedInventory = inventoryRepository.save(inventory);

        Assertions.assertNotNull(savedInventory);
        Assertions.assertEquals(10, savedInventory.getQuantity());
        Assertions.assertEquals(store.getStoreId(), savedInventory.getStore().getStoreId());
        Assertions.assertEquals(product.getProductId(), savedInventory.getProduct().getProductId());
    }
} 