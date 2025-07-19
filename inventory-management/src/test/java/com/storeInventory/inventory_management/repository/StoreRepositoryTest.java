package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @Test
    public void whenSavedStore_thenReturnNonNullStore() {
        StoreEntity store = new StoreEntity();
        store.setName("Test Store");
        store.setLocation("Test Location");

        StoreEntity savedStore = storeRepository.save(store);

        Assertions.assertNotNull(savedStore);
        Assertions.assertEquals("Test Store", savedStore.getName());
        Assertions.assertEquals("Test Location", savedStore.getLocation());
    }
} 