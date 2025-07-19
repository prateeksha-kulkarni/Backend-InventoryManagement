package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class InterStoreTransferRepositoryTest {

    @Autowired
    InterStoreTransferRepository interStoreTransferRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    public void whenSavedTransfer_thenReturnNonNullTransfer() {
        StoreEntity fromStore = new StoreEntity();
        fromStore.setName("From Store");
        fromStore.setLocation("Location 1");
        fromStore = storeRepository.save(fromStore);

        StoreEntity toStore = new StoreEntity();
        toStore.setName("To Store");
        toStore.setLocation("Location 2");
        toStore = storeRepository.save(toStore);

        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setSku("SKU123");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setDescription("A test product");
        product = productRepository.save(product);

        UserEntity requestedBy = UserEntity.builder()
                .username("user1")
                .password("password")
                .email("user1@example.com")
                .role(UserRole.MANAGER)
                .name("User One")
                .store(fromStore)
                .build();
        requestedBy = userRepository.save(requestedBy);

        InterStoreTransferEntity transfer = new InterStoreTransferEntity();
        transfer.setProduct(product);
        transfer.setFromStore(fromStore);
        transfer.setToStore(toStore);
        transfer.setQuantity(5);
        transfer.setStatus(TransferStatus.REQUESTED);
        transfer.setRequestedBy(requestedBy);

        InterStoreTransferEntity savedTransfer = interStoreTransferRepository.save(transfer);

        Assertions.assertNotNull(savedTransfer);
        Assertions.assertEquals(5, savedTransfer.getQuantity());
        Assertions.assertEquals(TransferStatus.REQUESTED, savedTransfer.getStatus());
        Assertions.assertEquals(product.getProductId(), savedTransfer.getProduct().getProductId());
        Assertions.assertEquals(fromStore.getStoreId(), savedTransfer.getFromStore().getStoreId());
        Assertions.assertEquals(toStore.getStoreId(), savedTransfer.getToStore().getStoreId());
        Assertions.assertEquals(requestedBy.getUserId(), savedTransfer.getRequestedBy().getUserId());
    }
} 