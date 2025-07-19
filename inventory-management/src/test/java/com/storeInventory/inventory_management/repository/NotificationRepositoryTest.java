package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.NotificationEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.NotificationRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class NotificationRepositoryTest {

    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    public void whenSavedNotification_thenReturnNonNullNotification() {
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

        NotificationEntity notification = new NotificationEntity();
        notification.setStore(store);
        notification.setProduct(product);
        notification.setType(NotificationEntity.NotificationType.LOW_STOCK);
        notification.setIsRead(false);

        NotificationEntity savedNotification = notificationRepository.save(notification);

        Assertions.assertNotNull(savedNotification);
        Assertions.assertEquals(NotificationEntity.NotificationType.LOW_STOCK, savedNotification.getType());
        Assertions.assertFalse(savedNotification.getIsRead());
        Assertions.assertEquals(store.getStoreId(), savedNotification.getStore().getStoreId());
        Assertions.assertEquals(product.getProductId(), savedNotification.getProduct().getProductId());
    }
} 