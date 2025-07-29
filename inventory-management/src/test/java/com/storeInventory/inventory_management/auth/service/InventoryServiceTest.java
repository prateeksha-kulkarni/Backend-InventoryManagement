package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.InventoryResponseDto;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StockAdjustmentRepository stockAdjustmentRepository;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryService = new InventoryService(inventoryRepository);
        inventoryService.productRepository = productRepository;
        inventoryService.storeRepository = storeRepository;
        inventoryService.userRepository = userRepository;
        inventoryService.stockAdjustmentRepository = stockAdjustmentRepository;
    }

    @Test
    void testGetAllInventory() {
        InventoryEntity inventory1 = mock(InventoryEntity.class);
        InventoryEntity inventory2 = mock(InventoryEntity.class);
        List<InventoryEntity> expectedInventories = Arrays.asList(inventory1, inventory2);
        doReturn(expectedInventories).when(inventoryRepository).findAll();
        List<InventoryEntity> result = inventoryService.getAllInventory();
        assertThat(result, equalTo(expectedInventories));
        assertThat(result, hasSize(2));
        verify(inventoryRepository, atLeast(1)).findAll();
    }

    @Test
    void testGetInventoryByStore() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        InventoryEntity inventory1 = mock(InventoryEntity.class);
        InventoryEntity inventory2 = mock(InventoryEntity.class);
        List<InventoryEntity> expectedInventories = Arrays.asList(inventory1, inventory2);
        doReturn(expectedInventories).when(inventoryRepository).findByStore_StoreId(storeId);
        List<InventoryEntity> result = inventoryService.getInventoryByStore(storeId);
        assertThat(result, equalTo(expectedInventories));
        assertThat(result, hasSize(2));
        verify(inventoryRepository, atLeast(1)).findByStore_StoreId(storeId);
    }

    @Test
    void testGetInventoryByStoreAndProduct() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        InventoryEntity inventory = mock(InventoryEntity.class);
        Optional<InventoryEntity> expectedInventory = Optional.of(inventory);
        doReturn(expectedInventory).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        Optional<InventoryEntity> result = inventoryService.getInventoryByStoreAndProduct(storeId, productId);
        assertThat(result, equalTo(expectedInventory));
        assertTrue(result.isPresent());
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
    }

    @Test
    void testSearchInventory() {
        String query = "test";
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        InventoryEntity inventory1 = mock(InventoryEntity.class);
        InventoryEntity inventory2 = mock(InventoryEntity.class);
        StoreEntity store1 = mock(StoreEntity.class);
        StoreEntity store2 = mock(StoreEntity.class);
        ProductEntity product1 = mock(ProductEntity.class);
        ProductEntity product2 = mock(ProductEntity.class);
        doReturn(store1).when(inventory1).getStore();
        doReturn(store2).when(inventory2).getStore();
        doReturn(product1).when(inventory1).getProduct();
        doReturn(product2).when(inventory2).getProduct();
        doReturn(storeId).when(store1).getStoreId();
        doReturn(storeId).when(store2).getStoreId();
        doReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174001")).when(product1).getProductId();
        doReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174002")).when(product2).getProductId();
        List<InventoryEntity> inventoryEntities = Arrays.asList(inventory1, inventory2);
        doReturn(inventoryEntities).when(inventoryRepository).searchInventory(query, storeId);
        List<InventoryResponseDto> result = inventoryService.searchInventory(query, storeId);
        assertThat(result, notNullValue());
        assertThat(result, hasSize(2));
        verify(inventoryRepository, atLeast(1)).searchInventory(query, storeId);
    }

    @Test
    void testGetInventoryByProduct() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        InventoryEntity inventory1 = mock(InventoryEntity.class);
        InventoryEntity inventory2 = mock(InventoryEntity.class);
        List<InventoryEntity> expectedInventories = Arrays.asList(inventory1, inventory2);
        doReturn(expectedInventories).when(inventoryRepository).findByProduct_ProductId(productId);
        List<InventoryEntity> result = inventoryService.getInventoryByProduct(productId);
        assertThat(result, equalTo(expectedInventories));
        assertThat(result, hasSize(2));
        verify(inventoryRepository, atLeast(1)).findByProduct_ProductId(productId);
    }

    @Test
    void testCreateInventorySuccess() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductEntity productEntity = mock(ProductEntity.class);
        StoreEntity storeEntity = mock(StoreEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        ProductEntity productFromInventory = mock(ProductEntity.class);
        StoreEntity storeFromInventory = mock(StoreEntity.class);
        doReturn(productFromInventory).when(inventory).getProduct();
        doReturn(productId).when(productFromInventory).getProductId();
        doReturn(storeFromInventory).when(inventory).getStore();
        doReturn(storeId).when(storeFromInventory).getStoreId();
        doReturn(Optional.of(productEntity)).when(productRepository).findById(productId);
        doReturn(Optional.of(storeEntity)).when(storeRepository).findById(storeId);
        doReturn(inventory).when(inventoryRepository).save(inventory);
        InventoryEntity result = inventoryService.createInventory(inventory);
        assertThat(result, equalTo(inventory));
        verify(productRepository, atLeast(1)).findById(productId);
        verify(storeRepository, atLeast(1)).findById(storeId);
        verify(inventory, atLeast(1)).setProduct(productEntity);
        verify(inventory, atLeast(1)).setStore(storeEntity);
        verify(inventoryRepository, atLeast(1)).save(inventory);
    }

    @Test
    void testCreateInventoryThrowsExceptionWhenProductIdMissing() {
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(null).when(inventory).getProduct();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryService.createInventory(inventory));
        assertEquals("❌ Product ID is missing in the request", exception.getMessage());
    }

    @Test
    void testCreateInventoryThrowsExceptionWhenProductMissingProductId() {
        InventoryEntity inventory = mock(InventoryEntity.class);
        ProductEntity productEntity = mock(ProductEntity.class);
        doReturn(productEntity).when(inventory).getProduct();
        doReturn(null).when(productEntity).getProductId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryService.createInventory(inventory));
        assertEquals("❌ Product ID is missing in the request", exception.getMessage());
    }

    @Test
    void testCreateInventoryThrowsExceptionWhenStoreIdMissing() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        InventoryEntity inventory = mock(InventoryEntity.class);
        ProductEntity productEntity = mock(ProductEntity.class);
        doReturn(productEntity).when(inventory).getProduct();
        doReturn(productId).when(productEntity).getProductId();
        doReturn(null).when(inventory).getStore();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryService.createInventory(inventory));
        assertEquals("❌ Store ID is missing in the request", exception.getMessage());
    }

    @Test
    void testCreateInventoryThrowsExceptionWhenStoreMissingStoreId() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        InventoryEntity inventory = mock(InventoryEntity.class);
        ProductEntity productEntity = mock(ProductEntity.class);
        StoreEntity storeEntity = mock(StoreEntity.class);
        doReturn(productEntity).when(inventory).getProduct();
        doReturn(productId).when(productEntity).getProductId();
        doReturn(storeEntity).when(inventory).getStore();
        doReturn(null).when(storeEntity).getStoreId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> inventoryService.createInventory(inventory));
        assertEquals("❌ Store ID is missing in the request", exception.getMessage());
    }

    @Test
    void testCreateInventoryThrowsExceptionWhenProductNotFound() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        InventoryEntity inventory = mock(InventoryEntity.class);
        ProductEntity productFromInventory = mock(ProductEntity.class);
        StoreEntity storeFromInventory = mock(StoreEntity.class);
        doReturn(productFromInventory).when(inventory).getProduct();
        doReturn(productId).when(productFromInventory).getProductId();
        doReturn(storeFromInventory).when(inventory).getStore();
        doReturn(storeId).when(storeFromInventory).getStoreId();
        doReturn(Optional.empty()).when(productRepository).findById(productId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.createInventory(inventory));
        assertEquals("Product not found: " + productId, exception.getMessage());
        verify(productRepository, atLeast(1)).findById(productId);
    }

    @Test
    void testCreateInventoryThrowsExceptionWhenStoreNotFound() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductEntity productEntity = mock(ProductEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        ProductEntity productFromInventory = mock(ProductEntity.class);
        StoreEntity storeFromInventory = mock(StoreEntity.class);
        doReturn(productFromInventory).when(inventory).getProduct();
        doReturn(productId).when(productFromInventory).getProductId();
        doReturn(storeFromInventory).when(inventory).getStore();
        doReturn(storeId).when(storeFromInventory).getStoreId();
        doReturn(Optional.of(productEntity)).when(productRepository).findById(productId);
        doReturn(Optional.empty()).when(storeRepository).findById(storeId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.createInventory(inventory));
        assertEquals("Store not found: " + storeId, exception.getMessage());
        verify(productRepository, atLeast(1)).findById(productId);
        verify(storeRepository, atLeast(1)).findById(storeId);
    }

    @Test
    void testAdjustQuantityAddSuccess() {
        String productName = "TestProduct";
        int quantity = 10;
        ChangeType type = ChangeType.ADD;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        ProductEntity productEntity = mock(ProductEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doReturn(Optional.of(productEntity)).when(productRepository).findByNameIgnoreCase(productName);
        doReturn(Optional.of(inventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        doReturn(20).when(inventory).getQuantity();
        doReturn(inventory).when(inventoryRepository).save(inventory);
        doReturn(Optional.of(userEntity)).when(userRepository).findById(userId);
        doReturn(userId).when(userEntity).getUserId();
        doReturn(mock(StockAdjustmentEntity.class)).when(stockAdjustmentRepository).save(any(StockAdjustmentEntity.class));
        InventoryEntity result = inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason);
        assertThat(result, equalTo(inventory));
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        verify(inventory, atLeast(1)).setQuantity(30);
        verify(inventoryRepository, atLeast(1)).save(inventory);
        verify(userRepository, atLeast(1)).findById(userId);
        verify(stockAdjustmentRepository, atLeast(1)).save(any(StockAdjustmentEntity.class));
    }

    @Test
    void testAdjustQuantityRemoveSuccess() {
        String productName = "TestProduct";
        int quantity = 10;
        ChangeType type = ChangeType.REMOVE;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        ProductEntity productEntity = mock(ProductEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        UserEntity userEntity = mock(UserEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doReturn(Optional.of(productEntity)).when(productRepository).findByNameIgnoreCase(productName);
        doReturn(Optional.of(inventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        doReturn(20).when(inventory).getQuantity();
        doReturn(inventory).when(inventoryRepository).save(inventory);
        doReturn(Optional.of(userEntity)).when(userRepository).findById(userId);
        doReturn(userId).when(userEntity).getUserId();
        doReturn(mock(StockAdjustmentEntity.class)).when(stockAdjustmentRepository).save(any(StockAdjustmentEntity.class));
        InventoryEntity result = inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason);
        assertThat(result, equalTo(inventory));
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        verify(inventory, atLeast(1)).setQuantity(10);
        verify(inventoryRepository, atLeast(1)).save(inventory);
        verify(userRepository, atLeast(1)).findById(userId);
        verify(stockAdjustmentRepository, atLeast(1)).save(any(StockAdjustmentEntity.class));
    }

    @Test
    void testAdjustQuantityThrowsExceptionWhenProductNotFound() {
        String productName = "NonExistentProduct";
        int quantity = 10;
        ChangeType type = ChangeType.ADD;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        doReturn(Optional.empty()).when(productRepository).findByNameIgnoreCase(productName);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason));
        assertEquals("Product not found: " + productName, exception.getMessage());
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
    }

    @Test
    void testAdjustQuantityThrowsExceptionWhenInventoryNotFound() {
        String productName = "TestProduct";
        int quantity = 10;
        ChangeType type = ChangeType.ADD;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        ProductEntity productEntity = mock(ProductEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doReturn(Optional.of(productEntity)).when(productRepository).findByNameIgnoreCase(productName);
        doReturn(Optional.empty()).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason));
        assertEquals("Inventory for this product does not exist at the given store. Please add the product first with a minThreshold.", exception.getMessage());
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
    }

    @Test
    void testAdjustQuantityThrowsExceptionWhenInsufficientStock() {
        String productName = "TestProduct";
        int quantity = 30;
        ChangeType type = ChangeType.REMOVE;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        ProductEntity productEntity = mock(ProductEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doReturn(Optional.of(productEntity)).when(productRepository).findByNameIgnoreCase(productName);
        doReturn(Optional.of(inventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        doReturn(20).when(inventory).getQuantity();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason));
        assertEquals("Insufficient stock to remove.", exception.getMessage());
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
    }

    @Test
    void testAdjustQuantityThrowsExceptionWhenUserNotFound() {
        String productName = "TestProduct";
        int quantity = 10;
        ChangeType type = ChangeType.ADD;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        ProductEntity productEntity = mock(ProductEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doReturn(Optional.of(productEntity)).when(productRepository).findByNameIgnoreCase(productName);
        doReturn(Optional.of(inventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        doReturn(20).when(inventory).getQuantity();
        doReturn(inventory).when(inventoryRepository).save(inventory);
        doReturn(Optional.empty()).when(userRepository).findById(userId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason));
        assertEquals("User not found: " + userId, exception.getMessage());
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        verify(inventory, atLeast(1)).setQuantity(30);
        verify(inventoryRepository, atLeast(1)).save(inventory);
        verify(userRepository, atLeast(1)).findById(userId);
    }

    @Test
    void testAdjustQuantityThrowsExceptionWhenInvalidAdjustmentType() {
        String productName = "TestProduct";
        int quantity = 10;
        ChangeType type = null;
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        String reason = "Test reason";
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        ProductEntity productEntity = mock(ProductEntity.class);
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doReturn(Optional.of(productEntity)).when(productRepository).findByNameIgnoreCase(productName);
        doReturn(Optional.of(inventory)).when(inventoryRepository).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
        doReturn(20).when(inventory).getQuantity();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> inventoryService.adjustQuantity(productName, quantity, type, storeId, userId, reason));
        assertEquals("Invalid adjustment type.", exception.getMessage());
        verify(productRepository, atLeast(1)).findByNameIgnoreCase(productName);
        verify(inventoryRepository, atLeast(1)).findByStore_StoreIdAndProduct_ProductId(storeId, productId);
    }
}
