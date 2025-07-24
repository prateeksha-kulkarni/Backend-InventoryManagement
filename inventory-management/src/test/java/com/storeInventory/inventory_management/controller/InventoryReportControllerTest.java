package com.storeInventory.inventory_management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.InventoryReportController;
import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.exception.GlobalExceptionHandler;
import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.service.InventoryReportService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {InventoryReportController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class InventoryReportControllerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private InventoryReportController inventoryReportController;

    @MockitoBean
    private InventoryReportService inventoryReportService;


    @Test
    @DisplayName("Test getTurnoverReport(InventoryTurnoverRequest)")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testGetTurnoverReport() throws Exception {

        InventoryTurnoverRequest inventoryTurnoverRequest = new InventoryTurnoverRequest();
        inventoryTurnoverRequest.setEndDate(LocalDate.of(1970, 1, 1));
        inventoryTurnoverRequest.setStartDate(LocalDate.of(1970, 1, 1));
        String content = new ObjectMapper().writeValueAsString(inventoryTurnoverRequest);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/inventory-analytics/turnover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(inventoryReportController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder);
    }


    @Test
    @DisplayName(
            "Test getTurnoverReport(InventoryTurnoverRequest); given UserEntity() Email is 'jane.doe@example.org'")
    @Tag("MaintainedByDiffblue")
    void testGetTurnoverReport_givenUserEntityEmailIsJaneDoeExampleOrg() {


        // Arrange
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);
        when(inventoryRepository.findAll()).thenReturn(new ArrayList<>());

        StoreEntity store = new StoreEntity();
        store.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store.setIncomingTransfers(new ArrayList<>());
        store.setInventories(new ArrayList<>());
        store.setLocation("Location");
        store.setName("Name");
        store.setNotifications(new ArrayList<>());
        store.setOutgoingTransfers(new ArrayList<>());
        store.setStoreId(UUID.randomUUID());
        store.setUsers(new ArrayList<>());

        UserEntity approvedBy = new UserEntity();
        approvedBy.setEmail("jane.doe@example.org");
        approvedBy.setName("Name");
        approvedBy.setPassword("iloveyou");
        approvedBy.setRole(UserRole.MANAGER);
        approvedBy.setStore(store);
        approvedBy.setUserId(UUID.randomUUID());
        approvedBy.setUsername("janedoe");

        StoreEntity fromStore = new StoreEntity();
        fromStore.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        fromStore.setIncomingTransfers(new ArrayList<>());
        fromStore.setInventories(new ArrayList<>());
        fromStore.setLocation("Location");
        fromStore.setName("Name");
        fromStore.setNotifications(new ArrayList<>());
        fromStore.setOutgoingTransfers(new ArrayList<>());
        fromStore.setStoreId(UUID.randomUUID());
        fromStore.setUsers(new ArrayList<>());

        ProductEntity product = new ProductEntity();
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setInventories(new ArrayList<>());
        product.setName("Name");
        product.setNotifications(new ArrayList<>());
        product.setProductId(UUID.randomUUID());
        product.setSku("Sku");
        product.setTransfers(new ArrayList<>());

        StoreEntity store2 = new StoreEntity();
        store2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store2.setIncomingTransfers(new ArrayList<>());
        store2.setInventories(new ArrayList<>());
        store2.setLocation("Location");
        store2.setName("Name");
        store2.setNotifications(new ArrayList<>());
        store2.setOutgoingTransfers(new ArrayList<>());
        store2.setStoreId(UUID.randomUUID());
        store2.setUsers(new ArrayList<>());

        UserEntity requestedBy = new UserEntity();
        requestedBy.setEmail("jane.doe@example.org");
        requestedBy.setName("Name");
        requestedBy.setPassword("iloveyou");
        requestedBy.setRole(UserRole.MANAGER);
        requestedBy.setStore(store2);
        requestedBy.setUserId(UUID.randomUUID());
        requestedBy.setUsername("janedoe");

        StoreEntity toStore = new StoreEntity();
        toStore.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        toStore.setIncomingTransfers(new ArrayList<>());
        toStore.setInventories(new ArrayList<>());
        toStore.setLocation("Location");
        toStore.setName("Name");
        toStore.setNotifications(new ArrayList<>());
        toStore.setOutgoingTransfers(new ArrayList<>());
        toStore.setStoreId(UUID.randomUUID());
        toStore.setUsers(new ArrayList<>());

        InterStoreTransferEntity interStoreTransferEntity = new InterStoreTransferEntity();
        interStoreTransferEntity.setApprovedBy(approvedBy);
        interStoreTransferEntity.setFromStore(fromStore);
        interStoreTransferEntity.setProduct(product);
        interStoreTransferEntity.setQuantity(3);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());

        ArrayList<InterStoreTransferEntity> interStoreTransferEntityList = new ArrayList<>();
        interStoreTransferEntityList.add(interStoreTransferEntity);
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.findByStatusAndTimestampBetween(
                Mockito.<TransferStatus>any(),
                Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any()))
                .thenReturn(interStoreTransferEntityList);
        InventoryReportController inventoryReportController =
                new InventoryReportController(
                        new InventoryReportService(inventoryRepository, transferRepository));

        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setEndDate(LocalDate.of(1970, 1, 1));
        request.setStartDate(LocalDate.of(1970, 1, 1));

        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> actualTurnoverReport =
                inventoryReportController.getTurnoverReport(request);

        // Assert
        verify(transferRepository)
                .findByStatusAndTimestampBetween(
                        eq(TransferStatus.COMPLETED), isA(LocalDateTime.class), isA(LocalDateTime.class));
        verify(inventoryRepository).findAll();
        HttpStatusCode statusCode = actualTurnoverReport.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualTurnoverReport.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualTurnoverReport.getBody().isEmpty());
        assertTrue(actualTurnoverReport.hasBody());
        assertTrue(actualTurnoverReport.getHeaders().isEmpty());
    }


    @Test
    @DisplayName(
            "Test getTurnoverReport(InventoryTurnoverRequest); then calls calculateTurnover(InventoryTurnoverRequest)")
    @Tag("MaintainedByDiffblue")
    void testGetTurnoverReport_thenCallsCalculateTurnover() {


        // Arrange
        InventoryReportService inventoryReportService = mock(InventoryReportService.class);
        when(inventoryReportService.calculateTurnover(Mockito.<InventoryTurnoverRequest>any()))
                .thenReturn(new ArrayList<>());
        InventoryReportController inventoryReportController =
                new InventoryReportController(inventoryReportService);

        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setEndDate(LocalDate.of(1970, 1, 1));
        request.setStartDate(LocalDate.of(1970, 1, 1));

        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> actualTurnoverReport =
                inventoryReportController.getTurnoverReport(request);

        // Assert
        verify(inventoryReportService).calculateTurnover(isA(InventoryTurnoverRequest.class));
        HttpStatusCode statusCode = actualTurnoverReport.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualTurnoverReport.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualTurnoverReport.getBody().isEmpty());
        assertTrue(actualTurnoverReport.hasBody());
        assertTrue(actualTurnoverReport.getHeaders().isEmpty());
    }


    @Test
    @DisplayName("Test getTurnoverReport(InventoryTurnoverRequest); then return Body size is one")
    @Tag("MaintainedByDiffblue")
    void testGetTurnoverReport_thenReturnBodySizeIsOne() {


        // Arrange
        ProductEntity product = new ProductEntity();
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setInventories(new ArrayList<>());
        product.setName("Name");
        product.setNotifications(new ArrayList<>());
        product.setProductId(UUID.randomUUID());
        product.setSku("Sku");
        product.setTransfers(new ArrayList<>());

        StoreEntity store = new StoreEntity();
        store.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store.setIncomingTransfers(new ArrayList<>());
        store.setInventories(new ArrayList<>());
        store.setLocation("Location");
        store.setName("Name");
        store.setNotifications(new ArrayList<>());
        store.setOutgoingTransfers(new ArrayList<>());
        store.setStoreId(UUID.randomUUID());
        store.setUsers(new ArrayList<>());

        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setInventoryId(UUID.randomUUID());
        inventoryEntity.setMinThreshold(1);
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(3);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<InventoryEntity> inventoryEntityList = new ArrayList<>();
        inventoryEntityList.add(inventoryEntity);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);
        when(inventoryRepository.findAll()).thenReturn(inventoryEntityList);
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.findByStatusAndTimestampBetween(
                Mockito.<TransferStatus>any(),
                Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());
        InventoryReportController inventoryReportController =
                new InventoryReportController(
                        new InventoryReportService(inventoryRepository, transferRepository));

        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setEndDate(LocalDate.of(1970, 1, 1));
        request.setStartDate(LocalDate.of(1970, 1, 1));

        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> actualTurnoverReport =
                inventoryReportController.getTurnoverReport(request);

        // Assert
        verify(transferRepository)
                .findByStatusAndTimestampBetween(
                        eq(TransferStatus.COMPLETED), isA(LocalDateTime.class), isA(LocalDateTime.class));
        verify(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> body = actualTurnoverReport.getBody();
        assertEquals(1, body.size());
        InventoryTurnoverResponse getResult = body.get(0);
        assertEquals("Overstock", getResult.getStockStatus());
        assertEquals(1, getResult.getMinThreshold());
        assertEquals(3, getResult.getQuantity());
    }


    @Test
    @DisplayName("Test getTurnoverReport(InventoryTurnoverRequest); then return Body size is two")
    @Tag("MaintainedByDiffblue")
    void testGetTurnoverReport_thenReturnBodySizeIsTwo() {


        // Arrange
        ProductEntity product = new ProductEntity();
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product.setDescription("The characteristics of someone or something");
        product.setInventories(new ArrayList<>());
        product.setName("Name");
        product.setNotifications(new ArrayList<>());
        UUID productId = UUID.randomUUID();
        product.setProductId(productId);
        product.setSku("Sku");
        product.setTransfers(new ArrayList<>());

        StoreEntity store = new StoreEntity();
        store.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store.setIncomingTransfers(new ArrayList<>());
        store.setInventories(new ArrayList<>());
        store.setLocation("Location");
        store.setName("Name");
        store.setNotifications(new ArrayList<>());
        store.setOutgoingTransfers(new ArrayList<>());
        UUID storeId = UUID.randomUUID();
        store.setStoreId(storeId);
        store.setUsers(new ArrayList<>());

        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setInventoryId(UUID.randomUUID());
        inventoryEntity.setMinThreshold(1);
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(3);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ProductEntity product2 = new ProductEntity();
        product2.setCategory(ProductCategory.CLOTHING);
        product2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product2.setDescription("Overstock");
        product2.setInventories(new ArrayList<>());
        product2.setName("Name");
        product2.setNotifications(new ArrayList<>());
        product2.setProductId(UUID.randomUUID());
        product2.setSku("Sku");
        product2.setTransfers(new ArrayList<>());

        StoreEntity store2 = new StoreEntity();
        store2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store2.setIncomingTransfers(new ArrayList<>());
        store2.setInventories(new ArrayList<>());
        store2.setLocation("Location");
        store2.setName("Name");
        store2.setNotifications(new ArrayList<>());
        store2.setOutgoingTransfers(new ArrayList<>());
        store2.setStoreId(UUID.randomUUID());
        store2.setUsers(new ArrayList<>());

        InventoryEntity inventoryEntity2 = new InventoryEntity();
        inventoryEntity2.setInventoryId(UUID.randomUUID());
        inventoryEntity2.setMinThreshold(3);
        inventoryEntity2.setProduct(product2);
        inventoryEntity2.setQuantity(1);
        inventoryEntity2.setStockAdjustments(new ArrayList<>());
        inventoryEntity2.setStore(store2);
        inventoryEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        ArrayList<InventoryEntity> inventoryEntityList = new ArrayList<>();
        inventoryEntityList.add(inventoryEntity2);
        inventoryEntityList.add(inventoryEntity);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);
        when(inventoryRepository.findAll()).thenReturn(inventoryEntityList);
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.findByStatusAndTimestampBetween(
                Mockito.<TransferStatus>any(),
                Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());
        InventoryReportController inventoryReportController =
                new InventoryReportController(
                        new InventoryReportService(inventoryRepository, transferRepository));

        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setEndDate(LocalDate.of(1970, 1, 1));
        request.setStartDate(LocalDate.of(1970, 1, 1));

        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> actualTurnoverReport =
                inventoryReportController.getTurnoverReport(request);

        // Assert
        verify(transferRepository)
                .findByStatusAndTimestampBetween(
                        eq(TransferStatus.COMPLETED), isA(LocalDateTime.class), isA(LocalDateTime.class));
        verify(inventoryRepository).findAll();
        List<InventoryTurnoverResponse> body = actualTurnoverReport.getBody();
        assertEquals(2, body.size());
        InventoryTurnoverResponse getResult = body.get(1);
        assertEquals("Name", getResult.getProductName());
        assertEquals("Name", getResult.getStoreName());
        assertEquals("Overstock", getResult.getStockStatus());
        InventoryTurnoverResponse getResult2 = body.get(0);
        assertEquals("Stockout", getResult2.getStockStatus());
        assertEquals(0.0d, getResult.getTurnoverRate());
        assertEquals(1, getResult.getMinThreshold());
        assertEquals(1, getResult2.getQuantity());
        assertEquals(3, getResult2.getMinThreshold());
        assertEquals(3, getResult.getQuantity());
        assertSame(productId, getResult.getProductId());
        assertSame(storeId, getResult.getStoreId());
    }


    @Test
    @DisplayName(
            "Test getTurnoverReport(InventoryTurnoverRequest); then StatusCode return HttpStatus")
    @Tag("MaintainedByDiffblue")
    void testGetTurnoverReport_thenStatusCodeReturnHttpStatus() {


        // Arrange
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);
        when(inventoryRepository.findAll()).thenReturn(new ArrayList<>());
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.findByStatusAndTimestampBetween(
                Mockito.<TransferStatus>any(),
                Mockito.<LocalDateTime>any(),
                Mockito.<LocalDateTime>any()))
                .thenReturn(new ArrayList<>());
        InventoryReportController inventoryReportController =
                new InventoryReportController(
                        new InventoryReportService(inventoryRepository, transferRepository));

        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setEndDate(LocalDate.of(1970, 1, 1));
        request.setStartDate(LocalDate.of(1970, 1, 1));

        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> actualTurnoverReport =
                inventoryReportController.getTurnoverReport(request);

        // Assert
        verify(transferRepository)
                .findByStatusAndTimestampBetween(
                        eq(TransferStatus.COMPLETED), isA(LocalDateTime.class), isA(LocalDateTime.class));
        verify(inventoryRepository).findAll();
        HttpStatusCode statusCode = actualTurnoverReport.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualTurnoverReport.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualTurnoverReport.getBody().isEmpty());
        assertTrue(actualTurnoverReport.hasBody());
        assertTrue(actualTurnoverReport.getHeaders().isEmpty());
    }
}
