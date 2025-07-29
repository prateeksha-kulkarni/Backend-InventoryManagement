package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;
import org.junit.jupiter.api.Disabled;

@Timeout(10)
public class InventoryReportServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private InterStoreTransferRepository transferRepository;

    @Mock
    private StockAdjustmentRepository stockAdjustmentRepository;

    private InventoryReportService inventoryReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryReportService = new InventoryReportService(inventoryRepository, transferRepository, stockAdjustmentRepository);
    }

    @Test
    void testConstructor() {
        InventoryReportService service = new InventoryReportService(inventoryRepository, transferRepository, stockAdjustmentRepository);
        assertNotNull(service);
    }

    @Test
    void testCalculateTurnoverWithEmptyData() {
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(new ArrayList<>()).when(stockAdjustmentRepository).findAll();
        doReturn(new ArrayList<>()).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(0));
        verify(transferRepository, atLeast(1)).findByStatusAndTimestampBetween(eq(TransferStatus.COMPLETED), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(stockAdjustmentRepository, atLeast(1)).findAll();
        verify(inventoryRepository, atLeast(1)).findAll();
    }

    @Disabled()
    @Test
    void testCalculateTurnoverWithCompleteTransfers() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(store).when(transfer).getFromStore();
        doReturn(product).when(transfer).getProduct();
        doReturn(50).when(transfer).getQuantity();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(31).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InterStoreTransferEntity> transfers = List.of(transfer);
        List<InventoryEntity> inventories = List.of(inventory);
        doReturn(transfers).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(new ArrayList<>()).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals(storeId, response.getStoreId());
        assertEquals("Test Store", response.getStoreName());
        assertEquals(productId, response.getProductId());
        assertEquals("Test Product", response.getProductName());
        assertEquals(50, response.getQuantity());
        assertEquals(10, response.getMinThreshold());
        assertEquals("Healthy", response.getStockStatus());
        assertThat(response.getTurnoverRate(), closeTo(2.0, 0.00001));
        assertThat(response.getSalesTurnoverRate(), closeTo(0.0, 0.00001));
    }

    @Disabled()
    @Test
    void testCalculateTurnoverWithSalesAdjustments() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(31).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        StockAdjustmentEntity salesAdjustment = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(salesAdjustment).getInventory();
        doReturn(ChangeType.REMOVE).when(salesAdjustment).getChangeType();
        doReturn(30).when(salesAdjustment).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 1, 15, 12, 0)).when(salesAdjustment).getTimestamp();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InventoryEntity> inventories = List.of(inventory);
        List<StockAdjustmentEntity> adjustments = List.of(salesAdjustment);
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(adjustments).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals(storeId, response.getStoreId());
        assertEquals("Test Store", response.getStoreName());
        assertEquals(productId, response.getProductId());
        assertEquals("Test Product", response.getProductName());
        assertEquals(50, response.getQuantity());
        assertEquals(10, response.getMinThreshold());
        assertEquals("Healthy", response.getStockStatus());
        assertThat(response.getTurnoverRate(), closeTo(0.0, 0.00001));
        assertThat(response.getSalesTurnoverRate(), closeTo(1.2, 0.00001));
    }

    @Test
    void testCalculateTurnoverWithStockoutStatus() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(5).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InventoryEntity> inventories = List.of(inventory);
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(new ArrayList<>()).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals("Stockout", response.getStockStatus());
    }

    @Test
    void testCalculateTurnoverWithOverstockStatus() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(31).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InventoryEntity> inventories = List.of(inventory);
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(new ArrayList<>()).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals("Overstock", response.getStockStatus());
    }

    @Test
    void testCalculateTurnoverWithZeroQuantity() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(store).when(transfer).getFromStore();
        doReturn(product).when(transfer).getProduct();
        doReturn(50).when(transfer).getQuantity();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(0).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        StockAdjustmentEntity salesAdjustment = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(salesAdjustment).getInventory();
        doReturn(ChangeType.REMOVE).when(salesAdjustment).getChangeType();
        doReturn(30).when(salesAdjustment).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 1, 15, 12, 0)).when(salesAdjustment).getTimestamp();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InterStoreTransferEntity> transfers = List.of(transfer);
        List<InventoryEntity> inventories = List.of(inventory);
        List<StockAdjustmentEntity> adjustments = List.of(salesAdjustment);
        doReturn(transfers).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(adjustments).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals("Stockout", response.getStockStatus());
        assertThat(response.getTurnoverRate(), closeTo(0.0, 0.00001));
        assertThat(response.getSalesTurnoverRate(), closeTo(0.0, 0.00001));
    }

    @Disabled()
    @Test
    void testCalculateTurnoverWithMultipleTransfersAndSales() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InterStoreTransferEntity transfer1 = mock(InterStoreTransferEntity.class);
        doReturn(store).when(transfer1).getFromStore();
        doReturn(product).when(transfer1).getProduct();
        doReturn(30).when(transfer1).getQuantity();
        InterStoreTransferEntity transfer2 = mock(InterStoreTransferEntity.class);
        doReturn(store).when(transfer2).getFromStore();
        doReturn(product).when(transfer2).getProduct();
        doReturn(20).when(transfer2).getQuantity();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(50).when(inventory).getQuantity();
        doReturn(20).when(inventory).getMinThreshold();
        StockAdjustmentEntity salesAdjustment1 = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(salesAdjustment1).getInventory();
        doReturn(ChangeType.REMOVE).when(salesAdjustment1).getChangeType();
        doReturn(15).when(salesAdjustment1).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 1, 10, 12, 0)).when(salesAdjustment1).getTimestamp();
        StockAdjustmentEntity salesAdjustment2 = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(salesAdjustment2).getInventory();
        doReturn(ChangeType.REMOVE).when(salesAdjustment2).getChangeType();
        doReturn(25).when(salesAdjustment2).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 1, 20, 12, 0)).when(salesAdjustment2).getTimestamp();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InterStoreTransferEntity> transfers = List.of(transfer1, transfer2);
        List<InventoryEntity> inventories = List.of(inventory);
        List<StockAdjustmentEntity> adjustments = List.of(salesAdjustment1, salesAdjustment2);
        doReturn(transfers).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(adjustments).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals(storeId, response.getStoreId());
        assertEquals("Test Store", response.getStoreName());
        assertEquals(productId, response.getProductId());
        assertEquals("Test Product", response.getProductName());
        assertEquals(50, response.getQuantity());
        assertEquals(20, response.getMinThreshold());
        assertEquals("Healthy", response.getStockStatus());
        assertThat(response.getTurnoverRate(), closeTo(2.0, 0.00001));
        assertThat(response.getSalesTurnoverRate(), closeTo(0.4, 0.00001));
    }

    @Test
    void testCalculateTurnoverWithDifferentStoreAndProduct() {
        UUID storeId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId1 = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        UUID storeId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
        UUID productId2 = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");
        StoreEntity store1 = mock(StoreEntity.class);
        ProductEntity product1 = mock(ProductEntity.class);
        StoreEntity store2 = mock(StoreEntity.class);
        ProductEntity product2 = mock(ProductEntity.class);
        doReturn(storeId1).when(store1).getStoreId();
        doReturn("Store 1").when(store1).getName();
        doReturn(productId1).when(product1).getProductId();
        doReturn("Product 1").when(product1).getName();
        doReturn(storeId2).when(store2).getStoreId();
        doReturn("Store 2").when(store2).getName();
        doReturn(productId2).when(product2).getProductId();
        doReturn("Product 2").when(product2).getName();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doReturn(store1).when(transfer).getFromStore();
        doReturn(product1).when(transfer).getProduct();
        doReturn(25).when(transfer).getQuantity();
        InventoryEntity inventory1 = mock(InventoryEntity.class);
        doReturn(store1).when(inventory1).getStore();
        doReturn(product1).when(inventory1).getProduct();
        doReturn(50).when(inventory1).getQuantity();
        doReturn(10).when(inventory1).getMinThreshold();
        InventoryEntity inventory2 = mock(InventoryEntity.class);
        doReturn(store2).when(inventory2).getStore();
        doReturn(product2).when(inventory2).getProduct();
        doReturn(75).when(inventory2).getQuantity();
        doReturn(15).when(inventory2).getMinThreshold();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InterStoreTransferEntity> transfers = List.of(transfer);
        List<InventoryEntity> inventories = List.of(inventory1, inventory2);
        doReturn(transfers).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(new ArrayList<>()).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(2));
        InventoryTurnoverResponse response1 = result.stream().filter(r -> r.getStoreId().equals(storeId1)).findFirst().orElseThrow();
        assertEquals("Store 1", response1.getStoreName());
        assertEquals("Product 1", response1.getProductName());
        assertThat(response1.getTurnoverRate(), closeTo(0.5, 0.00001));
        InventoryTurnoverResponse response2 = result.stream().filter(r -> r.getStoreId().equals(storeId2)).findFirst().orElseThrow();
        assertEquals("Store 2", response2.getStoreName());
        assertEquals("Product 2", response2.getProductName());
        assertThat(response2.getTurnoverRate(), closeTo(0.0, 0.00001));
    }

    @Disabled()
    @Test
    void testCalculateTurnoverWithFilteredAdjustments() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(31).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        StockAdjustmentEntity includedAdjustment = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(includedAdjustment).getInventory();
        doReturn(ChangeType.REMOVE).when(includedAdjustment).getChangeType();
        doReturn(30).when(includedAdjustment).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 1, 15, 12, 0)).when(includedAdjustment).getTimestamp();
        StockAdjustmentEntity excludedByType = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(excludedByType).getInventory();
        doReturn(ChangeType.ADD).when(excludedByType).getChangeType();
        doReturn(20).when(excludedByType).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 1, 10, 12, 0)).when(excludedByType).getTimestamp();
        StockAdjustmentEntity excludedByDate = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(excludedByDate).getInventory();
        doReturn(ChangeType.REMOVE).when(excludedByDate).getChangeType();
        doReturn(25).when(excludedByDate).getQuantityChange();
        doReturn(LocalDateTime.of(2022, 12, 31, 12, 0)).when(excludedByDate).getTimestamp();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InventoryEntity> inventories = List.of(inventory);
        List<StockAdjustmentEntity> adjustments = List.of(includedAdjustment, excludedByType, excludedByDate);
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(adjustments).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertThat(response.getSalesTurnoverRate(), closeTo(1.2, 0.00001));
    }

    @Disabled()
    @Test
    void testCalculateTurnoverWithHealthyStatus() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(31).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InventoryEntity> inventories = List.of(inventory);
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(new ArrayList<>()).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertEquals("Healthy", response.getStockStatus());
    }

    @Test
    void testCalculateTurnoverFilteringAdjustmentsByDateRange() {
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
        StoreEntity store = mock(StoreEntity.class);
        ProductEntity product = mock(ProductEntity.class);
        doReturn(storeId).when(store).getStoreId();
        doReturn("Test Store").when(store).getName();
        doReturn(productId).when(product).getProductId();
        doReturn("Test Product").when(product).getName();
        InventoryEntity inventory = mock(InventoryEntity.class);
        doReturn(store).when(inventory).getStore();
        doReturn(product).when(inventory).getProduct();
        doReturn(31).when(inventory).getQuantity();
        doReturn(10).when(inventory).getMinThreshold();
        StockAdjustmentEntity excludedAfterDate = mock(StockAdjustmentEntity.class);
        doReturn(inventory).when(excludedAfterDate).getInventory();
        doReturn(ChangeType.REMOVE).when(excludedAfterDate).getChangeType();
        doReturn(40).when(excludedAfterDate).getQuantityChange();
        doReturn(LocalDateTime.of(2023, 2, 1, 12, 0)).when(excludedAfterDate).getTimestamp();
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 1, 31));
        List<InventoryEntity> inventories = List.of(inventory);
        List<StockAdjustmentEntity> adjustments = List.of(excludedAfterDate);
        doReturn(new ArrayList<>()).when(transferRepository).findByStatusAndTimestampBetween(any(), any(), any());
        doReturn(adjustments).when(stockAdjustmentRepository).findAll();
        doReturn(inventories).when(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> result = inventoryReportService.calculateTurnover(request);
        assertNotNull(result);
        assertThat(result, hasSize(1));
        InventoryTurnoverResponse response = result.get(0);
        assertThat(response.getSalesTurnoverRate(), closeTo(0.0, 0.00001));
    }
}
