package com.storeInventory.inventory_management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.InventoryController;
import com.storeInventory.inventory_management.auth.dto.InventoryResponseDto;
import com.storeInventory.inventory_management.auth.dto.request.AdjustmentRequest;
import com.storeInventory.inventory_management.auth.exception.GlobalExceptionHandler;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.ProductStatus;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.service.InventoryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {InventoryController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class InventoryControllerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private InventoryController inventoryController;

    @MockitoBean
    private InventoryService inventoryService;

    /**
     * Test {@link InventoryController#getAllInventory()}.
     *
     * <ul>
     *   <li>Then content string {@code []}.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#getAllInventory()}
     */
    @Test
    @DisplayName("Test getAllInventory(); then content string '[]'")
    @Tag("MaintainedByDiffblue")
    void testGetAllInventory_thenContentStringLeftSquareBracketRightSquareBracket() throws Exception {
        // Arrange
        when(inventoryService.getAllInventory()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/inventory");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InventoryController#searchInventory(String, UUID, String)}.
     *
     * <ul>
     *   <li>Then content string {@code []}.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#searchInventory(String, UUID, String)}
     */
    @Test
    @DisplayName("Test searchInventory(String, UUID, String); then content string '[]'")
    @Tag("MaintainedByDiffblue")
    void testSearchInventory_thenContentStringLeftSquareBracketRightSquareBracket() throws Exception {
        // Arrange
        when(inventoryService.searchInventory(Mockito.<String>any(), Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/inventory/search").param("query", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InventoryController#getInventoryByStore(UUID)}.
     *
     * <ul>
     *   <li>Then content string {@code []}.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#getInventoryByStore(UUID)}
     */
    @Test
    @DisplayName("Test getInventoryByStore(UUID); then content string '[]'")
    @Tag("MaintainedByDiffblue")
    void testGetInventoryByStore_thenContentStringLeftSquareBracketRightSquareBracket()
            throws Exception {
        // Arrange
        when(inventoryService.getInventoryByStore(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/inventory/store/{storeId}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InventoryController#getInventoryByStoreAndProduct(UUID, UUID)}.
     *
     * <p>Method under test: {@link InventoryController#getInventoryByStoreAndProduct(UUID, UUID)}
     */
    @Test
    @DisplayName("Test getInventoryByStoreAndProduct(UUID, UUID)")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testGetInventoryByStoreAndProduct() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setInventoryId(UUID.randomUUID());
        inventoryEntity.setMinThreshold(1);

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
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());

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
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<InventoryEntity> ofResult = Optional.of(inventoryEntity);
        when(inventoryService.getInventoryByStoreAndProduct(Mockito.<UUID>any(), Mockito.<UUID>any()))
                .thenReturn(ofResult);
        UUID randomUUIDResult = UUID.randomUUID();
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(
                        "/api/inventory/store/{storeId}/product/{productId}",
                        randomUUIDResult,
                        UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"inventoryId\":\"dd67217b-84ff-4098-8947-27cbc810988b\",\"quantity\":1,\"minThreshold\":1,\"updatedAt\":[1970"
                                                + ",1,1,0,0]}"));
    }

    /**
     * Test {@link InventoryController#getInventoryByStoreAndProduct(UUID, UUID)}.
     *
     * <p>Method under test: {@link InventoryController#getInventoryByStoreAndProduct(UUID, UUID)}
     */
    @Test
    @DisplayName("Test getInventoryByStoreAndProduct(UUID, UUID)")
    @Tag("MaintainedByDiffblue")
    void testGetInventoryByStoreAndProduct2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

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
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<InventoryEntity> ofResult = Optional.of(inventoryEntity);
        InventoryRepository inventoryRepository = mock(InventoryRepository.class);
        when(inventoryRepository.findByStore_StoreIdAndProduct_ProductId(
                Mockito.<UUID>any(), Mockito.<UUID>any()))
                .thenReturn(ofResult);
        InventoryController inventoryController =
                new InventoryController(new InventoryService(inventoryRepository));
        UUID storeId = UUID.randomUUID();

        // Act
        ResponseEntity<Optional<InventoryEntity>> actualInventoryByStoreAndProduct =
                inventoryController.getInventoryByStoreAndProduct(storeId, UUID.randomUUID());

        // Assert
        verify(inventoryRepository)
                .findByStore_StoreIdAndProduct_ProductId(isA(UUID.class), isA(UUID.class));
        HttpStatusCode statusCode = actualInventoryByStoreAndProduct.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualInventoryByStoreAndProduct.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualInventoryByStoreAndProduct.hasBody());
        assertTrue(actualInventoryByStoreAndProduct.getHeaders().isEmpty());
        assertSame(ofResult, actualInventoryByStoreAndProduct.getBody());
    }

    /**
     * Test {@link InventoryController#getInventoryByStoreAndProduct(UUID, UUID)}.
     *
     * <ul>
     *   <li>Then calls {@link InventoryService#getInventoryByStoreAndProduct(UUID, UUID)}.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#getInventoryByStoreAndProduct(UUID, UUID)}
     */
    @Test
    @DisplayName(
            "Test getInventoryByStoreAndProduct(UUID, UUID); then calls getInventoryByStoreAndProduct(UUID, UUID)")
    @Tag("MaintainedByDiffblue")
    void testGetInventoryByStoreAndProduct_thenCallsGetInventoryByStoreAndProduct() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

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
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        Optional<InventoryEntity> ofResult = Optional.of(inventoryEntity);
        InventoryService inventoryService = mock(InventoryService.class);
        when(inventoryService.getInventoryByStoreAndProduct(Mockito.<UUID>any(), Mockito.<UUID>any()))
                .thenReturn(ofResult);
        InventoryController inventoryController = new InventoryController(inventoryService);
        UUID storeId = UUID.randomUUID();

        // Act
        ResponseEntity<Optional<InventoryEntity>> actualInventoryByStoreAndProduct =
                inventoryController.getInventoryByStoreAndProduct(storeId, UUID.randomUUID());

        // Assert
        verify(inventoryService).getInventoryByStoreAndProduct(isA(UUID.class), isA(UUID.class));
        HttpStatusCode statusCode = actualInventoryByStoreAndProduct.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualInventoryByStoreAndProduct.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualInventoryByStoreAndProduct.hasBody());
        assertTrue(actualInventoryByStoreAndProduct.getHeaders().isEmpty());
        assertSame(ofResult, actualInventoryByStoreAndProduct.getBody());
    }

    /**
     * Test {@link InventoryController#getInventoryByProduct(UUID)}.
     *
     * <ul>
     *   <li>Then content string {@code []}.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#getInventoryByProduct(UUID)}
     */
    @Test
    @DisplayName("Test getInventoryByProduct(UUID); then content string '[]'")
    @Tag("MaintainedByDiffblue")
    void testGetInventoryByProduct_thenContentStringLeftSquareBracketRightSquareBracket()
            throws Exception {
        // Arrange
        when(inventoryService.getInventoryByProduct(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/inventory/product/{productId}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InventoryController#createInventory(InventoryEntity)}.
     *
     * <p>Method under test: {@link InventoryController#createInventory(InventoryEntity)}
     */
    @Test
    @DisplayName("Test createInventory(InventoryEntity)")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testCreateInventory() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type
        // `java.time.LocalDateTime` not supported by default: add Module
        // "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (or disable
        // `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`) (through reference chain:
        // com.storeInventory.inventory_management.auth.model.InventoryEntity["updatedAt"])
        //       at
        // com.fasterxml.jackson.databind.exc.InvalidDefinitionException.from(InvalidDefinitionException.java:77)
        //       at
        // com.fasterxml.jackson.databind.SerializerProvider.reportBadDefinition(SerializerProvider.java:1359)
        //       at
        // com.fasterxml.jackson.databind.ser.impl.UnsupportedTypeSerializer.serialize(UnsupportedTypeSerializer.java:34)
        //       at
        // com.fasterxml.jackson.databind.ser.BeanPropertyWriter.serializeAsField(BeanPropertyWriter.java:732)
        //       at
        // com.fasterxml.jackson.databind.ser.std.BeanSerializerBase.serializeFields(BeanSerializerBase.java:760)
        //       at com.fasterxml.jackson.databind.ser.BeanSerializer.serialize(BeanSerializer.java:183)
        //       at
        // com.fasterxml.jackson.databind.ser.DefaultSerializerProvider._serialize(DefaultSerializerProvider.java:503)
        //       at
        // com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.serializeValue(DefaultSerializerProvider.java:342)
        //       at
        // com.fasterxml.jackson.databind.ObjectMapper._writeValueAndClose(ObjectMapper.java:4859)
        //       at
        // com.fasterxml.jackson.databind.ObjectMapper.writeValueAsString(ObjectMapper.java:4079)
        //   See https://diff.blue/R013 to resolve this issue.

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
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        String content = new ObjectMapper().writeValueAsString(inventoryEntity);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder);
    }

    /**
     * Test {@link InventoryController#createInventory(InventoryEntity)}.
     *
     * <ul>
     *   <li>Then StatusCode return {@link HttpStatus}.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#createInventory(InventoryEntity)}
     */
    @Test
    @DisplayName("Test createInventory(InventoryEntity); then StatusCode return HttpStatus")
    @Tag("MaintainedByDiffblue")
    void testCreateInventory_thenStatusCodeReturnHttpStatus() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

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
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        InventoryService inventoryService = mock(InventoryService.class);
        when(inventoryService.createInventory(Mockito.<InventoryEntity>any()))
                .thenReturn(inventoryEntity);
        InventoryController inventoryController = new InventoryController(inventoryService);

        ProductEntity product2 = new ProductEntity();
        product2.setCategory(ProductCategory.ELECTRONICS);
        product2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        product2.setDescription("The characteristics of someone or something");
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

        InventoryEntity inventory = new InventoryEntity();
        inventory.setInventoryId(UUID.randomUUID());
        inventory.setMinThreshold(1);
        inventory.setProduct(product2);
        inventory.setQuantity(1);
        inventory.setStockAdjustments(new ArrayList<>());
        inventory.setStore(store2);
        inventory.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());

        // Act
        ResponseEntity<InventoryEntity> actualCreateInventoryResult =
                inventoryController.createInventory(inventory);

        // Assert
        verify(inventoryService).createInventory(isA(InventoryEntity.class));
        HttpStatusCode statusCode = actualCreateInventoryResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualCreateInventoryResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualCreateInventoryResult.hasBody());
        assertTrue(actualCreateInventoryResult.getHeaders().isEmpty());
        assertSame(inventoryEntity, actualCreateInventoryResult.getBody());
    }

    /**
     * Test {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}.
     *
     * <ul>
     *   <li>Given {@link InventoryEntity#InventoryEntity()} MinThreshold is one.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}
     */
    @Test
    @DisplayName(
            "Test adjustQuantity(AdjustmentRequest, String); given InventoryEntity() MinThreshold is one")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testAdjustQuantity_givenInventoryEntityMinThresholdIsOne() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange
        InventoryEntity inventoryEntity = new InventoryEntity();
        inventoryEntity.setInventoryId(UUID.randomUUID());
        inventoryEntity.setMinThreshold(1);

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
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());

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
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        when(inventoryService.adjustQuantity(
                Mockito.<String>any(),
                anyInt(),
                Mockito.<ChangeType>any(),
                Mockito.<UUID>any(),
                Mockito.<UUID>any(),
                Mockito.<String>any()))
                .thenReturn(inventoryEntity);
        MockHttpServletRequestBuilder contentTypeResult =
                MockMvcRequestBuilders.put("/api/inventory/adjust/{productName}", "Product Name")
                        .contentType(MediaType.APPLICATION_JSON);

        AdjustmentRequest adjustmentRequest = new AdjustmentRequest();
        adjustmentRequest.setQuantity(1);
        adjustmentRequest.setReason("Just cause");
        adjustmentRequest.setStoreId(UUID.randomUUID());
        adjustmentRequest.setType(ChangeType.ADD);
        adjustmentRequest.setUserId(UUID.randomUUID());
        MockHttpServletRequestBuilder requestBuilder =
                contentTypeResult.content(new ObjectMapper().writeValueAsString(adjustmentRequest));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(inventoryController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"inventoryId\":\"18496b20-6d98-44fc-bc0a-809dc5030c73\",\"store\":{\"storeId\":\"44c03784-fa1c-49a9-a77e"
                                                + "-ae451a779b41\",\"name\":\"Name\",\"location\":\"Location\"},\"product\":{\"productId\":\"9b8c0ba2-235c-42f1-9754"
                                                + "-27c8a5b7751e\",\"name\":\"Name\",\"sku\":\"Sku\",\"category\":\"ELECTRONICS\"},\"quantity\":1,\"minThreshold\":1,"
                                                + "\"status\":\"IN_STOCK\",\"updatedAt\":[1970,1,1,0,0]}"));
    }

    /**
     * Test {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}.
     *
     * <ul>
     *   <li>Then return Body Quantity intValue is minus one.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}
     */
    @Test
    @DisplayName(
            "Test adjustQuantity(AdjustmentRequest, String); then return Body Quantity intValue is minus one")
    @Tag("MaintainedByDiffblue")
    void testAdjustQuantity_thenReturnBodyQuantityIntValueIsMinusOne() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

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

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        storeEntity.setIncomingTransfers(new ArrayList<>());
        storeEntity.setInventories(new ArrayList<>());
        storeEntity.setLocation("Location");
        storeEntity.setName("Name");
        storeEntity.setNotifications(new ArrayList<>());
        storeEntity.setOutgoingTransfers(new ArrayList<>());
        storeEntity.setStoreId(UUID.randomUUID());
        storeEntity.setUsers(new ArrayList<>());

        ProductEntity productEntity = new ProductEntity();
        productEntity.setCategory(ProductCategory.ELECTRONICS);
        productEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        productEntity.setDescription("The characteristics of someone or something");
        productEntity.setInventories(new ArrayList<>());
        productEntity.setName("Name");
        productEntity.setNotifications(new ArrayList<>());
        productEntity.setProductId(UUID.randomUUID());
        productEntity.setSku("Sku");
        productEntity.setTransfers(new ArrayList<>());
        InventoryEntity inventoryEntity = mock(InventoryEntity.class);
        when(inventoryEntity.getProduct()).thenReturn(productEntity);
        when(inventoryEntity.getStore()).thenReturn(storeEntity);
        when(inventoryEntity.getMinThreshold()).thenReturn(1);
        when(inventoryEntity.getQuantity()).thenReturn(-1);
        when(inventoryEntity.getUpdatedAt()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        UUID randomUUIDResult = UUID.randomUUID();
        when(inventoryEntity.getInventoryId()).thenReturn(randomUUIDResult);
        doNothing().when(inventoryEntity).setInventoryId(Mockito.<UUID>any());
        doNothing().when(inventoryEntity).setMinThreshold(Mockito.<Integer>any());
        doNothing().when(inventoryEntity).setProduct(Mockito.<ProductEntity>any());
        doNothing().when(inventoryEntity).setQuantity(Mockito.<Integer>any());
        doNothing()
                .when(inventoryEntity)
                .setStockAdjustments(Mockito.<List<StockAdjustmentEntity>>any());
        doNothing().when(inventoryEntity).setStore(Mockito.<StoreEntity>any());
        doNothing().when(inventoryEntity).setUpdatedAt(Mockito.<LocalDateTime>any());
        inventoryEntity.setInventoryId(UUID.randomUUID());
        inventoryEntity.setMinThreshold(1);
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        InventoryService inventoryService = mock(InventoryService.class);
        when(inventoryService.adjustQuantity(
                Mockito.<String>any(),
                anyInt(),
                Mockito.<ChangeType>any(),
                Mockito.<UUID>any(),
                Mockito.<UUID>any(),
                Mockito.<String>any()))
                .thenReturn(inventoryEntity);
        InventoryController inventoryController = new InventoryController(inventoryService);

        AdjustmentRequest request = new AdjustmentRequest();
        request.setQuantity(1);
        request.setReason("Just cause");
        request.setStoreId(UUID.randomUUID());
        request.setType(ChangeType.ADD);
        request.setUserId(UUID.randomUUID());

        // Act
        ResponseEntity<InventoryResponseDto> actualAdjustQuantityResult =
                inventoryController.adjustQuantity(request, "Product Name");

        // Assert
        verify(inventoryEntity).getInventoryId();
        verify(inventoryEntity, atLeast(1)).getMinThreshold();
        verify(inventoryEntity, atLeast(1)).getProduct();
        verify(inventoryEntity, atLeast(1)).getQuantity();
        verify(inventoryEntity, atLeast(1)).getStore();
        verify(inventoryEntity).getUpdatedAt();
        verify(inventoryEntity).setInventoryId(isA(UUID.class));
        verify(inventoryEntity).setMinThreshold(eq(1));
        verify(inventoryEntity).setProduct(isA(ProductEntity.class));
        verify(inventoryEntity).setQuantity(eq(1));
        verify(inventoryEntity).setStockAdjustments(isA(List.class));
        verify(inventoryEntity).setStore(isA(StoreEntity.class));
        verify(inventoryEntity).setUpdatedAt(isA(LocalDateTime.class));
        verify(inventoryService)
                .adjustQuantity(
                        eq("Product Name"),
                        eq(1),
                        eq(ChangeType.ADD),
                        isA(UUID.class),
                        isA(UUID.class),
                        eq("Just cause"));
        HttpStatusCode statusCode = actualAdjustQuantityResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        InventoryResponseDto body = actualAdjustQuantityResult.getBody();
        assertEquals(-1, body.getQuantity().intValue());
        assertEquals(1, body.getMinThreshold().intValue());
        assertEquals(200, actualAdjustQuantityResult.getStatusCodeValue());
        assertEquals(ProductStatus.LOW_STOCK, body.getStatus());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualAdjustQuantityResult.hasBody());
        assertTrue(actualAdjustQuantityResult.getHeaders().isEmpty());
        assertSame(randomUUIDResult, body.getInventoryId());
    }

    /**
     * Test {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}.
     *
     * <ul>
     *   <li>Then return Body Quantity intValue is one.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}
     */
    @Test
    @DisplayName(
            "Test adjustQuantity(AdjustmentRequest, String); then return Body Quantity intValue is one")
    @Tag("MaintainedByDiffblue")
    void testAdjustQuantity_thenReturnBodyQuantityIntValueIsOne() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

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
        UUID inventoryId = UUID.randomUUID();
        inventoryEntity.setInventoryId(inventoryId);
        inventoryEntity.setMinThreshold(1);
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        InventoryService inventoryService = mock(InventoryService.class);
        when(inventoryService.adjustQuantity(
                Mockito.<String>any(),
                anyInt(),
                Mockito.<ChangeType>any(),
                Mockito.<UUID>any(),
                Mockito.<UUID>any(),
                Mockito.<String>any()))
                .thenReturn(inventoryEntity);
        InventoryController inventoryController = new InventoryController(inventoryService);

        AdjustmentRequest request = new AdjustmentRequest();
        request.setQuantity(1);
        request.setReason("Just cause");
        request.setStoreId(UUID.randomUUID());
        request.setType(ChangeType.ADD);
        request.setUserId(UUID.randomUUID());

        // Act
        ResponseEntity<InventoryResponseDto> actualAdjustQuantityResult =
                inventoryController.adjustQuantity(request, "Product Name");

        // Assert
        verify(inventoryService)
                .adjustQuantity(
                        eq("Product Name"),
                        eq(1),
                        eq(ChangeType.ADD),
                        isA(UUID.class),
                        isA(UUID.class),
                        eq("Just cause"));
        HttpStatusCode statusCode = actualAdjustQuantityResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        InventoryResponseDto body = actualAdjustQuantityResult.getBody();
        assertEquals(1, body.getMinThreshold().intValue());
        assertEquals(1, body.getQuantity().intValue());
        assertEquals(200, actualAdjustQuantityResult.getStatusCodeValue());
        assertEquals(ProductStatus.IN_STOCK, body.getStatus());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualAdjustQuantityResult.hasBody());
        assertTrue(actualAdjustQuantityResult.getHeaders().isEmpty());
        assertSame(inventoryId, body.getInventoryId());
    }

    /**
     * Test {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}.
     *
     * <ul>
     *   <li>Then return Body Quantity intValue is zero.
     * </ul>
     *
     * <p>Method under test: {@link InventoryController#adjustQuantity(AdjustmentRequest, String)}
     */
    @Test
    @DisplayName(
            "Test adjustQuantity(AdjustmentRequest, String); then return Body Quantity intValue is zero")
    @Tag("MaintainedByDiffblue")
    void testAdjustQuantity_thenReturnBodyQuantityIntValueIsZero() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

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

        StoreEntity storeEntity = new StoreEntity();
        storeEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        storeEntity.setIncomingTransfers(new ArrayList<>());
        storeEntity.setInventories(new ArrayList<>());
        storeEntity.setLocation("Location");
        storeEntity.setName("Name");
        storeEntity.setNotifications(new ArrayList<>());
        storeEntity.setOutgoingTransfers(new ArrayList<>());
        storeEntity.setStoreId(UUID.randomUUID());
        storeEntity.setUsers(new ArrayList<>());

        ProductEntity productEntity = new ProductEntity();
        productEntity.setCategory(ProductCategory.ELECTRONICS);
        productEntity.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        productEntity.setDescription("The characteristics of someone or something");
        productEntity.setInventories(new ArrayList<>());
        productEntity.setName("Name");
        productEntity.setNotifications(new ArrayList<>());
        productEntity.setProductId(UUID.randomUUID());
        productEntity.setSku("Sku");
        productEntity.setTransfers(new ArrayList<>());
        InventoryEntity inventoryEntity = mock(InventoryEntity.class);
        when(inventoryEntity.getProduct()).thenReturn(productEntity);
        when(inventoryEntity.getStore()).thenReturn(storeEntity);
        when(inventoryEntity.getMinThreshold()).thenReturn(1);
        when(inventoryEntity.getQuantity()).thenReturn(0);
        when(inventoryEntity.getUpdatedAt()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        UUID randomUUIDResult = UUID.randomUUID();
        when(inventoryEntity.getInventoryId()).thenReturn(randomUUIDResult);
        doNothing().when(inventoryEntity).setInventoryId(Mockito.<UUID>any());
        doNothing().when(inventoryEntity).setMinThreshold(Mockito.<Integer>any());
        doNothing().when(inventoryEntity).setProduct(Mockito.<ProductEntity>any());
        doNothing().when(inventoryEntity).setQuantity(Mockito.<Integer>any());
        doNothing()
                .when(inventoryEntity)
                .setStockAdjustments(Mockito.<List<StockAdjustmentEntity>>any());
        doNothing().when(inventoryEntity).setStore(Mockito.<StoreEntity>any());
        doNothing().when(inventoryEntity).setUpdatedAt(Mockito.<LocalDateTime>any());
        inventoryEntity.setInventoryId(UUID.randomUUID());
        inventoryEntity.setMinThreshold(1);
        inventoryEntity.setProduct(product);
        inventoryEntity.setQuantity(1);
        inventoryEntity.setStockAdjustments(new ArrayList<>());
        inventoryEntity.setStore(store);
        inventoryEntity.setUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        InventoryService inventoryService = mock(InventoryService.class);
        when(inventoryService.adjustQuantity(
                Mockito.<String>any(),
                anyInt(),
                Mockito.<ChangeType>any(),
                Mockito.<UUID>any(),
                Mockito.<UUID>any(),
                Mockito.<String>any()))
                .thenReturn(inventoryEntity);
        InventoryController inventoryController = new InventoryController(inventoryService);

        AdjustmentRequest request = new AdjustmentRequest();
        request.setQuantity(1);
        request.setReason("Just cause");
        request.setStoreId(UUID.randomUUID());
        request.setType(ChangeType.ADD);
        request.setUserId(UUID.randomUUID());

        // Act
        ResponseEntity<InventoryResponseDto> actualAdjustQuantityResult =
                inventoryController.adjustQuantity(request, "Product Name");

        // Assert
        verify(inventoryEntity).getInventoryId();
        verify(inventoryEntity, atLeast(1)).getMinThreshold();
        verify(inventoryEntity, atLeast(1)).getProduct();
        verify(inventoryEntity, atLeast(1)).getQuantity();
        verify(inventoryEntity, atLeast(1)).getStore();
        verify(inventoryEntity).getUpdatedAt();
        verify(inventoryEntity).setInventoryId(isA(UUID.class));
        verify(inventoryEntity).setMinThreshold(eq(1));
        verify(inventoryEntity).setProduct(isA(ProductEntity.class));
        verify(inventoryEntity).setQuantity(eq(1));
        verify(inventoryEntity).setStockAdjustments(isA(List.class));
        verify(inventoryEntity).setStore(isA(StoreEntity.class));
        verify(inventoryEntity).setUpdatedAt(isA(LocalDateTime.class));
        verify(inventoryService)
                .adjustQuantity(
                        eq("Product Name"),
                        eq(1),
                        eq(ChangeType.ADD),
                        isA(UUID.class),
                        isA(UUID.class),
                        eq("Just cause"));
        HttpStatusCode statusCode = actualAdjustQuantityResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        InventoryResponseDto body = actualAdjustQuantityResult.getBody();
        assertEquals(0, body.getQuantity().intValue());
        assertEquals(1, body.getMinThreshold().intValue());
        assertEquals(200, actualAdjustQuantityResult.getStatusCodeValue());
        assertEquals(ProductStatus.REORDER_SOON, body.getStatus());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualAdjustQuantityResult.hasBody());
        assertTrue(actualAdjustQuantityResult.getHeaders().isEmpty());
        assertSame(randomUUIDResult, body.getInventoryId());
    }
}
