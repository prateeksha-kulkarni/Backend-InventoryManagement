package com.storeInventory.inventory_management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.InterStoreTransferController;
import com.storeInventory.inventory_management.auth.exception.GlobalExceptionHandler;
import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import com.storeInventory.inventory_management.auth.repository.UserRepository;
import com.storeInventory.inventory_management.auth.service.InterStoreTransferService;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {InterStoreTransferController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class InterStoreTransferControllerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Autowired
    private InterStoreTransferController interStoreTransferController;

    @MockitoBean
    private InterStoreTransferService interStoreTransferService;

    /**
     * Test {@link InterStoreTransferController#getAllTransfers()}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getAllTransfers()}
     */
    @Test
    @DisplayName("Test getAllTransfers(); then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testGetAllTransfers_thenStatusIsOk() throws Exception {
        // Arrange
        when(interStoreTransferService.getAllTransfers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/transfers/logs");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InterStoreTransferController#getTransfersToStore(UUID, TransferStatus)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getTransfersToStore(UUID,
     * TransferStatus)}
     */
    @Test
    @DisplayName("Test getTransfersToStore(UUID, TransferStatus); then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testGetTransfersToStore_thenStatusIsOk() throws Exception {
        // Arrange
        when(interStoreTransferService.getTransfersToStore(
                Mockito.<UUID>any(), Mockito.<TransferStatus>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult =
                MockMvcRequestBuilders.get("/api/transfers/to/{storeId}", UUID.randomUUID());
        MockHttpServletRequestBuilder requestBuilder =
                getResult.param("status", String.valueOf(TransferStatus.REQUESTED));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InterStoreTransferController#getTransfersFromStore(UUID)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getTransfersFromStore(UUID)}
     */
    @Test
    @DisplayName("Test getTransfersFromStore(UUID); then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testGetTransfersFromStore_thenStatusIsOk() throws Exception {
        // Arrange
        when(interStoreTransferService.getTransfersFromStore(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/transfers/from/{storeId}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName("Test createTransfer(InterStoreTransferEntity)")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                mock(InterStoreTransferRepository.class),
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        when(transfer.getRequestedBy()).thenThrow(new IllegalStateException("foo"));

        // Act and Assert
        assertThrows(
                IllegalStateException.class, () -> interStoreTransferController.createTransfer(transfer));
        verify(transfer).getRequestedBy();
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName("Test createTransfer(InterStoreTransferEntity)")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
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

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setName("Name");
        userEntity.setPassword("iloveyou");
        userEntity.setRole(UserRole.MANAGER);
        userEntity.setStore(store);
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                mock(InterStoreTransferRepository.class),
                                mock(InventoryRepository.class),
                                userRepository,
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));

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

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setName("Name");
        userEntity2.setPassword("iloveyou");
        userEntity2.setRole(UserRole.MANAGER);
        userEntity2.setStore(store2);
        userEntity2.setUserId(UUID.randomUUID());
        userEntity2.setUsername("Requested By");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doThrow(new IllegalStateException("foo"))
                .when(transfer)
                .setRequestedBy(Mockito.<UserEntity>any());
        when(transfer.getRequestedBy()).thenReturn(userEntity2);

        // Act and Assert
        assertThrows(
                IllegalStateException.class, () -> interStoreTransferController.createTransfer(transfer));
        verify(transfer, atLeast(1)).getRequestedBy();
        verify(transfer).setRequestedBy(isA(UserEntity.class));
        verify(userRepository).findByUsername(eq("Requested By"));
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName("Test createTransfer(InterStoreTransferEntity)")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer3() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type
        // `java.time.LocalDateTime` not supported by default: add Module
        // "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (or disable
        // `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`) (through reference chain:
        // com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity["timestamp"])
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        String content = new ObjectMapper().writeValueAsString(interStoreTransferEntity);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/transfers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder);
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>Given {@link UserEntity#UserEntity()} Username is {@code null}.
     *   <li>Then calls {@link InterStoreTransferEntity#getApprovedBy()}.
     * </ul>
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test createTransfer(InterStoreTransferEntity); given UserEntity() Username is 'null'; then calls getApprovedBy()")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer_givenUserEntityUsernameIsNull_thenCallsGetApprovedBy() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.save(Mockito.<InterStoreTransferEntity>any()))
                .thenReturn(interStoreTransferEntity);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                transferRepository,
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));

        StoreEntity store3 = new StoreEntity();
        store3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store3.setIncomingTransfers(new ArrayList<>());
        store3.setInventories(new ArrayList<>());
        store3.setLocation("Location");
        store3.setName("Name");
        store3.setNotifications(new ArrayList<>());
        store3.setOutgoingTransfers(new ArrayList<>());
        store3.setStoreId(UUID.randomUUID());
        store3.setUsers(new ArrayList<>());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setName("Name");
        userEntity.setPassword("iloveyou");
        userEntity.setRole(UserRole.MANAGER);
        userEntity.setStore(store3);
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setUsername(null);

        StoreEntity store4 = new StoreEntity();
        store4.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store4.setIncomingTransfers(new ArrayList<>());
        store4.setInventories(new ArrayList<>());
        store4.setLocation("Location");
        store4.setName("Name");
        store4.setNotifications(new ArrayList<>());
        store4.setOutgoingTransfers(new ArrayList<>());
        store4.setStoreId(UUID.randomUUID());
        store4.setUsers(new ArrayList<>());

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setName("Name");
        userEntity2.setPassword("iloveyou");
        userEntity2.setRole(UserRole.MANAGER);
        userEntity2.setStore(store4);
        userEntity2.setUserId(UUID.randomUUID());
        userEntity2.setUsername(null);
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        when(transfer.getApprovedBy()).thenReturn(userEntity);
        when(transfer.getRequestedBy()).thenReturn(userEntity2);

        // Act
        interStoreTransferController.createTransfer(transfer);

        // Assert
        verify(transfer, atLeast(1)).getApprovedBy();
        verify(transfer, atLeast(1)).getRequestedBy();
        verify(transferRepository).save(isA(InterStoreTransferEntity.class));
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>Then calls {@link InterStoreTransferService#createTransfer(InterStoreTransferEntity)}.
     * </ul>
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test createTransfer(InterStoreTransferEntity); then calls createTransfer(InterStoreTransferEntity)")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer_thenCallsCreateTransfer() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        InterStoreTransferService transferService = mock(InterStoreTransferService.class);
        when(transferService.createTransfer(Mockito.<InterStoreTransferEntity>any()))
                .thenReturn(interStoreTransferEntity);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(transferService);

        StoreEntity fromStore2 = new StoreEntity();
        fromStore2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        fromStore2.setIncomingTransfers(new ArrayList<>());
        fromStore2.setInventories(new ArrayList<>());
        fromStore2.setLocation("Location");
        fromStore2.setName("Name");
        fromStore2.setNotifications(new ArrayList<>());
        fromStore2.setOutgoingTransfers(new ArrayList<>());
        fromStore2.setStoreId(UUID.randomUUID());
        fromStore2.setUsers(new ArrayList<>());

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

        StoreEntity toStore2 = new StoreEntity();
        toStore2.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        toStore2.setIncomingTransfers(new ArrayList<>());
        toStore2.setInventories(new ArrayList<>());
        toStore2.setLocation("Location");
        toStore2.setName("Name");
        toStore2.setNotifications(new ArrayList<>());
        toStore2.setOutgoingTransfers(new ArrayList<>());
        toStore2.setStoreId(UUID.randomUUID());
        toStore2.setUsers(new ArrayList<>());

        StoreEntity store3 = new StoreEntity();
        store3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store3.setIncomingTransfers(new ArrayList<>());
        store3.setInventories(new ArrayList<>());
        store3.setLocation("Location");
        store3.setName("Name");
        store3.setNotifications(new ArrayList<>());
        store3.setOutgoingTransfers(new ArrayList<>());
        store3.setStoreId(UUID.randomUUID());
        store3.setUsers(new ArrayList<>());

        UserEntity approvedBy2 = new UserEntity();
        approvedBy2.setEmail("jane.doe@example.org");
        approvedBy2.setName("Name");
        approvedBy2.setPassword("iloveyou");
        approvedBy2.setRole(UserRole.MANAGER);
        approvedBy2.setStore(store3);
        approvedBy2.setUserId(UUID.randomUUID());
        approvedBy2.setUsername("janedoe");

        InterStoreTransferEntity transfer = new InterStoreTransferEntity();
        transfer.setFromStore(fromStore2);
        transfer.setProduct(product2);
        transfer.setQuantity(1);
        transfer.setStatus(TransferStatus.REQUESTED);
        transfer.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        transfer.setToStore(toStore2);
        transfer.setTransferId(UUID.randomUUID());
        transfer.setRequestedBy(null);
        transfer.setApprovedBy(approvedBy2);

        // Act
        interStoreTransferController.createTransfer(transfer);

        // Assert
        verify(transferService).createTransfer(isA(InterStoreTransferEntity.class));
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>Then calls {@link InterStoreTransferEntity#setApprovedBy(UserEntity)}.
     * </ul>
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test createTransfer(InterStoreTransferEntity); then calls setApprovedBy(UserEntity)")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer_thenCallsSetApprovedBy() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.save(Mockito.<InterStoreTransferEntity>any()))
                .thenReturn(interStoreTransferEntity);

        StoreEntity store3 = new StoreEntity();
        store3.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store3.setIncomingTransfers(new ArrayList<>());
        store3.setInventories(new ArrayList<>());
        store3.setLocation("Location");
        store3.setName("Name");
        store3.setNotifications(new ArrayList<>());
        store3.setOutgoingTransfers(new ArrayList<>());
        store3.setStoreId(UUID.randomUUID());
        store3.setUsers(new ArrayList<>());

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setName("Name");
        userEntity.setPassword("iloveyou");
        userEntity.setRole(UserRole.MANAGER);
        userEntity.setStore(store3);
        userEntity.setUserId(UUID.randomUUID());
        userEntity.setUsername("janedoe");
        Optional<UserEntity> ofResult = Optional.of(userEntity);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.findByUsername(Mockito.<String>any())).thenReturn(ofResult);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                transferRepository,
                                mock(InventoryRepository.class),
                                userRepository,
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));

        StoreEntity store4 = new StoreEntity();
        store4.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store4.setIncomingTransfers(new ArrayList<>());
        store4.setInventories(new ArrayList<>());
        store4.setLocation("Location");
        store4.setName("Name");
        store4.setNotifications(new ArrayList<>());
        store4.setOutgoingTransfers(new ArrayList<>());
        store4.setStoreId(UUID.randomUUID());
        store4.setUsers(new ArrayList<>());

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setName("Name");
        userEntity2.setPassword("iloveyou");
        userEntity2.setRole(UserRole.MANAGER);
        userEntity2.setStore(store4);
        userEntity2.setUserId(UUID.randomUUID());
        userEntity2.setUsername("Approved By");

        StoreEntity store5 = new StoreEntity();
        store5.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        store5.setIncomingTransfers(new ArrayList<>());
        store5.setInventories(new ArrayList<>());
        store5.setLocation("Location");
        store5.setName("Name");
        store5.setNotifications(new ArrayList<>());
        store5.setOutgoingTransfers(new ArrayList<>());
        store5.setStoreId(UUID.randomUUID());
        store5.setUsers(new ArrayList<>());

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setName("Name");
        userEntity3.setPassword("iloveyou");
        userEntity3.setRole(UserRole.MANAGER);
        userEntity3.setStore(store5);
        userEntity3.setUserId(UUID.randomUUID());
        userEntity3.setUsername("Requested By");
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doNothing().when(transfer).setApprovedBy(Mockito.<UserEntity>any());
        doNothing().when(transfer).setRequestedBy(Mockito.<UserEntity>any());
        when(transfer.getApprovedBy()).thenReturn(userEntity2);
        when(transfer.getRequestedBy()).thenReturn(userEntity3);

        // Act
        interStoreTransferController.createTransfer(transfer);

        // Assert
        verify(transfer, atLeast(1)).getApprovedBy();
        verify(transfer, atLeast(1)).getRequestedBy();
        verify(transfer).setApprovedBy(isA(UserEntity.class));
        verify(transfer).setRequestedBy(isA(UserEntity.class));
        verify(userRepository, atLeast(1)).findByUsername(Mockito.<String>any());
        verify(transferRepository).save(isA(InterStoreTransferEntity.class));
    }

    /**
     * Test {@link InterStoreTransferController#createTransfer(InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>When {@link InterStoreTransferEntity} {@link InterStoreTransferEntity#getApprovedBy()}
     *       return {@code null}.
     * </ul>
     *
     * <p>Method under test: {@link
     * InterStoreTransferController#createTransfer(InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test createTransfer(InterStoreTransferEntity); when InterStoreTransferEntity getApprovedBy() return 'null'")
    @Tag("MaintainedByDiffblue")
    void testCreateTransfer_whenInterStoreTransferEntityGetApprovedByReturnNull() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.save(Mockito.<InterStoreTransferEntity>any()))
                .thenReturn(interStoreTransferEntity);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                transferRepository,
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        when(transfer.getApprovedBy()).thenReturn(null);
        when(transfer.getRequestedBy()).thenReturn(null);

        // Act
        interStoreTransferController.createTransfer(transfer);

        // Assert
        verify(transfer).getApprovedBy();
        verify(transfer).getRequestedBy();
        verify(transferRepository).save(isA(InterStoreTransferEntity.class));
    }

    /**
     * Test {@link InterStoreTransferController#updateTransfer(UUID, InterStoreTransferEntity)}.
     *
     * <p>Method under test: {@link InterStoreTransferController#updateTransfer(UUID,
     * InterStoreTransferEntity)}
     */
    @Test
    @DisplayName("Test updateTransfer(UUID, InterStoreTransferEntity)")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testUpdateTransfer() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type
        // `java.time.LocalDateTime` not supported by default: add Module
        // "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (or disable
        // `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`) (through reference chain:
        // com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity["timestamp"])
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        String content = new ObjectMapper().writeValueAsString(interStoreTransferEntity);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/api/transfers/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder);
    }

    /**
     * Test {@link InterStoreTransferController#updateTransfer(UUID, InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>Given {@link IllegalStateException#IllegalStateException(String)} with {@code foo}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#updateTransfer(UUID,
     * InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test updateTransfer(UUID, InterStoreTransferEntity); given IllegalStateException(String) with 'foo'")
    @Tag("MaintainedByDiffblue")
    void testUpdateTransfer_givenIllegalStateExceptionWithFoo() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        Optional<InterStoreTransferEntity> ofResult = Optional.of(mock(InterStoreTransferEntity.class));
        when(transferRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                transferRepository,
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));
        UUID id = UUID.randomUUID();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        when(transfer.getStatus()).thenThrow(new IllegalStateException("foo"));
        when(transfer.getTransferId()).thenReturn(UUID.randomUUID());
        doNothing().when(transfer).setTransferId(Mockito.<UUID>any());

        // Act and Assert
        assertThrows(
                IllegalStateException.class,
                () -> interStoreTransferController.updateTransfer(id, transfer));
        verify(transfer).getStatus();
        verify(transfer, atLeast(1)).getTransferId();
        verify(transfer).setTransferId(isA(UUID.class));
        verify(transferRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link InterStoreTransferController#updateTransfer(UUID, InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>Given {@link IllegalStateException#IllegalStateException(String)} with {@code transferId
     *       is required}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#updateTransfer(UUID,
     * InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test updateTransfer(UUID, InterStoreTransferEntity); given IllegalStateException(String) with 'transferId is required'")
    @Tag("MaintainedByDiffblue")
    void testUpdateTransfer_givenIllegalStateExceptionWithTransferIdIsRequired() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                mock(InterStoreTransferRepository.class),
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));
        UUID id = UUID.randomUUID();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        doThrow(new IllegalStateException("transferId is required"))
                .when(transfer)
                .setTransferId(Mockito.<UUID>any());

        // Act and Assert
        assertThrows(
                IllegalStateException.class,
                () -> interStoreTransferController.updateTransfer(id, transfer));
        verify(transfer).setTransferId(isA(UUID.class));
    }

    /**
     * Test {@link InterStoreTransferController#updateTransfer(UUID, InterStoreTransferEntity)}.
     *
     * <ul>
     *   <li>Then calls {@link InterStoreTransferEntity#setStatus(TransferStatus)}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#updateTransfer(UUID,
     * InterStoreTransferEntity)}
     */
    @Test
    @DisplayName(
            "Test updateTransfer(UUID, InterStoreTransferEntity); then calls setStatus(TransferStatus)")
    @Tag("MaintainedByDiffblue")
    void testUpdateTransfer_thenCallsSetStatus() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        InterStoreTransferEntity interStoreTransferEntity = mock(InterStoreTransferEntity.class);
        doThrow(new IllegalStateException("foo"))
                .when(interStoreTransferEntity)
                .setStatus(Mockito.<TransferStatus>any());
        Optional<InterStoreTransferEntity> ofResult = Optional.of(interStoreTransferEntity);
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        when(transferRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                transferRepository,
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));
        UUID id = UUID.randomUUID();
        InterStoreTransferEntity transfer = mock(InterStoreTransferEntity.class);
        when(transfer.getStatus()).thenReturn(TransferStatus.REQUESTED);
        when(transfer.getTransferId()).thenReturn(UUID.randomUUID());
        doNothing().when(transfer).setTransferId(Mockito.<UUID>any());

        // Act and Assert
        assertThrows(
                IllegalStateException.class,
                () -> interStoreTransferController.updateTransfer(id, transfer));
        verify(transfer).getStatus();
        verify(transfer, atLeast(1)).getTransferId();
        verify(interStoreTransferEntity).setStatus(eq(TransferStatus.REQUESTED));
        verify(transfer).setTransferId(isA(UUID.class));
        verify(transferRepository).findById(isA(UUID.class));
    }

    /**
     * Test {@link InterStoreTransferController#getTransferById(UUID)}.
     *
     * <ul>
     *   <li>Given {@link UserEntity#UserEntity()} Email is {@code jane.doe@example.org}.
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getTransferById(UUID)}
     */
    @Test
    @DisplayName(
            "Test getTransferById(UUID); given UserEntity() Email is 'jane.doe@example.org'; then status isOk()")
    @Disabled("TODO: Complete this test")
    @Tag("MaintainedByDiffblue")
    void testGetTransferById_givenUserEntityEmailIsJaneDoeExampleOrg_thenStatusIsOk()
            throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test

        // Arrange
        InterStoreTransferEntity interStoreTransferEntity = new InterStoreTransferEntity();

        UserEntity approvedBy = new UserEntity();
        approvedBy.setEmail("jane.doe@example.org");
        approvedBy.setName("Name");
        approvedBy.setPassword("iloveyou");
        approvedBy.setRole(UserRole.MANAGER);

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
        approvedBy.setStore(store);
        approvedBy.setUserId(UUID.randomUUID());
        approvedBy.setUsername("janedoe");
        interStoreTransferEntity.setApprovedBy(approvedBy);

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
        interStoreTransferEntity.setFromStore(fromStore);

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
        interStoreTransferEntity.setProduct(product);
        interStoreTransferEntity.setQuantity(1);

        UserEntity requestedBy = new UserEntity();
        requestedBy.setEmail("jane.doe@example.org");
        requestedBy.setName("Name");
        requestedBy.setPassword("iloveyou");
        requestedBy.setRole(UserRole.MANAGER);

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
        requestedBy.setStore(store2);
        requestedBy.setUserId(UUID.randomUUID());
        requestedBy.setUsername("janedoe");
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());

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
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        when(interStoreTransferService.getTransferById(Mockito.<UUID>any()))
                .thenReturn(interStoreTransferEntity);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/transfers/{id}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"transferId\":\"6e6da896-583b-420a-b289-53f757948fb2\",\"quantity\":1,\"status\":\"REQUESTED\",\"timestamp\":"
                                                + "[1970,1,1,0,0]}"));
    }

    /**
     * Test {@link InterStoreTransferController#getTransferById(UUID)}.
     *
     * <ul>
     *   <li>Then return Body is {@link InterStoreTransferEntity#InterStoreTransferEntity()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getTransferById(UUID)}
     */
    @Test
    @DisplayName("Test getTransferById(UUID); then return Body is InterStoreTransferEntity()")
    @Tag("MaintainedByDiffblue")
    void testGetTransferById_thenReturnBodyIsInterStoreTransferEntity() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
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
        interStoreTransferEntity.setQuantity(1);
        interStoreTransferEntity.setRequestedBy(requestedBy);
        interStoreTransferEntity.setStatus(TransferStatus.REQUESTED);
        interStoreTransferEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        interStoreTransferEntity.setToStore(toStore);
        interStoreTransferEntity.setTransferId(UUID.randomUUID());
        InterStoreTransferService transferService = mock(InterStoreTransferService.class);
        when(transferService.getTransferById(Mockito.<UUID>any())).thenReturn(interStoreTransferEntity);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(transferService);

        // Act
        ResponseEntity<InterStoreTransferEntity> actualTransferById =
                interStoreTransferController.getTransferById(UUID.randomUUID());

        // Assert
        verify(transferService).getTransferById(isA(UUID.class));
        assertSame(interStoreTransferEntity, actualTransferById.getBody());
    }

    /**
     * Test {@link InterStoreTransferController#getTransferById(UUID)}.
     *
     * <ul>
     *   <li>Then StatusCode return {@link HttpStatus}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getTransferById(UUID)}
     */
    @Test
    @DisplayName("Test getTransferById(UUID); then StatusCode return HttpStatus")
    @Tag("MaintainedByDiffblue")
    void testGetTransferById_thenStatusCodeReturnHttpStatus() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
        //   Run dcover create --keep-partial-tests to gain insights into why
        //   a non-Spring test was created.

        // Arrange
        InterStoreTransferRepository transferRepository = mock(InterStoreTransferRepository.class);
        Optional<InterStoreTransferEntity> ofResult = Optional.of(mock(InterStoreTransferEntity.class));
        when(transferRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        InterStoreTransferController interStoreTransferController =
                new InterStoreTransferController(
                        new InterStoreTransferService(
                                transferRepository,
                                mock(InventoryRepository.class),
                                mock(UserRepository.class),
                                mock(StoreRepository.class),
                                mock(ProductRepository.class)));

        // Act
        ResponseEntity<InterStoreTransferEntity> actualTransferById =
                interStoreTransferController.getTransferById(UUID.randomUUID());

        // Assert
        verify(transferRepository).findById(isA(UUID.class));
        HttpStatusCode statusCode = actualTransferById.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualTransferById.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualTransferById.hasBody());
        assertTrue(actualTransferById.getHeaders().isEmpty());
    }

    /**
     * Test {@link InterStoreTransferController#getTransferHistory(UUID)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getTransferHistory(UUID)}
     */
    @Test
    @DisplayName("Test getTransferHistory(UUID); then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testGetTransferHistory_thenStatusIsOk() throws Exception {
        // Arrange
        when(interStoreTransferService.getTransferHistoryForStore(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/transfers/history/{storeId}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link InterStoreTransferController#rejectTransfer(UUID)}.
     *
     * <ul>
     *   <li>Then status four hundred.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#rejectTransfer(UUID)}
     */
    @Test
    @DisplayName("Test rejectTransfer(UUID); then status four hundred")
    @Tag("MaintainedByDiffblue")
    void testRejectTransfer_thenStatusFourHundred() throws Exception {
        // Arrange
        doThrow(new IllegalStateException("Transfer rejected successfully."))
                .when(interStoreTransferService)
                .rejectTransfer(Mockito.<UUID>any());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/api/transfers/{transferId}/reject", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Transfer rejected successfully."));
    }

    /**
     * Test {@link InterStoreTransferController#rejectTransfer(UUID)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isNotFound()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#rejectTransfer(UUID)}
     */
    @Test
    @DisplayName("Test rejectTransfer(UUID); then status isNotFound()")
    @Tag("MaintainedByDiffblue")
    void testRejectTransfer_thenStatusIsNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("foo"))
                .when(interStoreTransferService)
                .rejectTransfer(Mockito.<UUID>any());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/api/transfers/{transferId}/reject", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Test {@link InterStoreTransferController#rejectTransfer(UUID)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#rejectTransfer(UUID)}
     */
    @Test
    @DisplayName("Test rejectTransfer(UUID); then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testRejectTransfer_thenStatusIsOk() throws Exception {
        // Arrange
        doNothing().when(interStoreTransferService).rejectTransfer(Mockito.<UUID>any());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.put("/api/transfers/{transferId}/reject", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Transfer rejected successfully."));
    }

    /**
     * Test {@link InterStoreTransferController#getPendingTransfers(UUID, TransferStatus)}.
     *
     * <ul>
     *   <li>Then status {@link StatusResultMatchers#isOk()}.
     * </ul>
     *
     * <p>Method under test: {@link InterStoreTransferController#getPendingTransfers(UUID,
     * TransferStatus)}
     */
    @Test
    @DisplayName("Test getPendingTransfers(UUID, TransferStatus); then status isOk()")
    @Tag("MaintainedByDiffblue")
    void testGetPendingTransfers_thenStatusIsOk() throws Exception {
        // Arrange
        when(interStoreTransferService.getTransfersToStoreByStatus(
                Mockito.<UUID>any(), Mockito.<TransferStatus>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult =
                MockMvcRequestBuilders.get("/api/transfers/api/transfers/to/{storeId}", UUID.randomUUID());
        MockHttpServletRequestBuilder requestBuilder =
                getResult.param("status", String.valueOf(TransferStatus.REQUESTED));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(interStoreTransferController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
