package com.storeInventory.inventory_management.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.ChangeLogController;
import com.storeInventory.inventory_management.auth.exception.GlobalExceptionHandler;
import com.storeInventory.inventory_management.auth.model.ChangeLogEntity;
import com.storeInventory.inventory_management.auth.model.Enum.UserRole;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.model.UserEntity;
import com.storeInventory.inventory_management.auth.repository.ChangeLogRepository;
import com.storeInventory.inventory_management.auth.service.ChangeLogService;

import java.time.LocalDate;
import java.util.ArrayList;
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

@ContextConfiguration(classes = {ChangeLogController.class, GlobalExceptionHandler.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class ChangeLogControllerTest {
    @Autowired
    private ChangeLogController changeLogController;

    @MockitoBean
    private ChangeLogService changeLogService;

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * Test {@link ChangeLogController#getAllChangeLogs()}.
     *
     * <p>Method under test: {@link ChangeLogController#getAllChangeLogs()}
     */
    @Test
    @DisplayName("Test getAllChangeLogs()")
    void testGetAllChangeLogs() throws Exception {
        // Arrange
        when(changeLogService.getAllChangeLogs()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/changelog");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(changeLogController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ChangeLogController#getChangeLogsByRecordId(UUID)}.
     *
     * <p>Method under test: {@link ChangeLogController#getChangeLogsByRecordId(UUID)}
     */
    @Test
    @DisplayName("Test getChangeLogsByRecordId(UUID)")
    void testGetChangeLogsByRecordId() throws Exception {
        // Arrange
        when(changeLogService.getChangeLogsByRecordId(Mockito.<UUID>any()))
                .thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/api/changelog/record/{recordId}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(changeLogController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Test {@link ChangeLogController#createChangeLog(ChangeLogEntity)}.
     *
     * <p>Method under test: {@link ChangeLogController#createChangeLog(ChangeLogEntity)}
     */
    @Test
    @DisplayName("Test createChangeLog(ChangeLogEntity)")
    @Disabled("TODO: Complete this test")
    void testCreateChangeLog() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Java 8 date/time type
        // `java.time.LocalDateTime` not supported by default: add Module
        // "com.fasterxml.jackson.datatype:jackson-datatype-jsr310" to enable handling (or disable
        // `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`) (through reference chain:
        // com.storeInventory.inventory_management.auth.model.ChangeLogEntity["timestamp"])
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
        UserEntity user = new UserEntity();
        user.setEmail("jane.doe@example.org");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRole(UserRole.MANAGER);
        user.setStore(store);
        user.setUserId(UUID.randomUUID());
        user.setUsername("janedoe");
        ChangeLogEntity changeLogEntity = new ChangeLogEntity();
        changeLogEntity.setChangeSummary("Change Summary");
        changeLogEntity.setLogId(UUID.randomUUID());
        changeLogEntity.setRecordId(UUID.randomUUID());
        changeLogEntity.setTableName("Table Name");
        changeLogEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        changeLogEntity.setUser(user);
        String content = new ObjectMapper().writeValueAsString(changeLogEntity);
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/api/changelog")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content);

        // Act
        MockMvcBuilders.standaloneSetup(changeLogController)
                .setControllerAdvice(globalExceptionHandler)
                .build()
                .perform(requestBuilder);
    }

    /**
     * Test {@link ChangeLogController#createChangeLog(ChangeLogEntity)}.
     *
     * <ul>
     *   <li>Then calls {@link ChangeLogService#createChangeLog(ChangeLogEntity)}.
     * </ul>
     *
     * <p>Method under test: {@link ChangeLogController#createChangeLog(ChangeLogEntity)}
     */
    @Test
    @DisplayName("Test createChangeLog(ChangeLogEntity); then calls createChangeLog(ChangeLogEntity)")
    @Tag("MaintainedByDiffblue")
    void testCreateChangeLog_thenCallsCreateChangeLog() {
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

        UserEntity user = new UserEntity();
        user.setEmail("jane.doe@example.org");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRole(UserRole.MANAGER);
        user.setStore(store);
        user.setUserId(UUID.randomUUID());
        user.setUsername("janedoe");

        ChangeLogEntity changeLogEntity = new ChangeLogEntity();
        changeLogEntity.setChangeSummary("Change Summary");
        changeLogEntity.setLogId(UUID.randomUUID());
        changeLogEntity.setRecordId(UUID.randomUUID());
        changeLogEntity.setTableName("Table Name");
        changeLogEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        changeLogEntity.setUser(user);
        ChangeLogService changeLogService = mock(ChangeLogService.class);
        when(changeLogService.createChangeLog(Mockito.<ChangeLogEntity>any()))
                .thenReturn(changeLogEntity);
        ChangeLogController changeLogController = new ChangeLogController(changeLogService);

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

        UserEntity user2 = new UserEntity();
        user2.setEmail("jane.doe@example.org");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setRole(UserRole.MANAGER);
        user2.setStore(store2);
        user2.setUserId(UUID.randomUUID());
        user2.setUsername("janedoe");

        ChangeLogEntity changeLog = new ChangeLogEntity();
        changeLog.setChangeSummary("Change Summary");
        changeLog.setLogId(UUID.randomUUID());
        changeLog.setRecordId(UUID.randomUUID());
        changeLog.setTableName("Table Name");
        changeLog.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        changeLog.setUser(user2);

        // Act
        ResponseEntity<ChangeLogEntity> actualCreateChangeLogResult =
                changeLogController.createChangeLog(changeLog);

        // Assert
        verify(changeLogService).createChangeLog(isA(ChangeLogEntity.class));
        HttpStatusCode statusCode = actualCreateChangeLogResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualCreateChangeLogResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualCreateChangeLogResult.hasBody());
        assertTrue(actualCreateChangeLogResult.getHeaders().isEmpty());
        assertSame(changeLogEntity, actualCreateChangeLogResult.getBody());
    }

    /**
     * Test {@link ChangeLogController#createChangeLog(ChangeLogEntity)}.
     *
     * <ul>
     *   <li>Then calls {@link ChangeLogRepository#save(Object)}.
     * </ul>
     *
     * <p>Method under test: {@link ChangeLogController#createChangeLog(ChangeLogEntity)}
     */
    @Test
    @DisplayName("Test createChangeLog(ChangeLogEntity); then calls save(Object)")
    @Tag("MaintainedByDiffblue")
    void testCreateChangeLog_thenCallsSave() {
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

        UserEntity user = new UserEntity();
        user.setEmail("jane.doe@example.org");
        user.setName("Name");
        user.setPassword("iloveyou");
        user.setRole(UserRole.MANAGER);
        user.setStore(store);
        user.setUserId(UUID.randomUUID());
        user.setUsername("janedoe");

        ChangeLogEntity changeLogEntity = new ChangeLogEntity();
        changeLogEntity.setChangeSummary("Change Summary");
        changeLogEntity.setLogId(UUID.randomUUID());
        changeLogEntity.setRecordId(UUID.randomUUID());
        changeLogEntity.setTableName("Table Name");
        changeLogEntity.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        changeLogEntity.setUser(user);
        ChangeLogRepository changeLogRepository = mock(ChangeLogRepository.class);
        when(changeLogRepository.save(Mockito.<ChangeLogEntity>any())).thenReturn(changeLogEntity);
        ChangeLogController changeLogController =
                new ChangeLogController(new ChangeLogService(changeLogRepository));

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

        UserEntity user2 = new UserEntity();
        user2.setEmail("jane.doe@example.org");
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setRole(UserRole.MANAGER);
        user2.setStore(store2);
        user2.setUserId(UUID.randomUUID());
        user2.setUsername("janedoe");

        ChangeLogEntity changeLog = new ChangeLogEntity();
        changeLog.setChangeSummary("Change Summary");
        changeLog.setLogId(UUID.randomUUID());
        changeLog.setRecordId(UUID.randomUUID());
        changeLog.setTableName("Table Name");
        changeLog.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        changeLog.setUser(user2);

        // Act
        ResponseEntity<ChangeLogEntity> actualCreateChangeLogResult =
                changeLogController.createChangeLog(changeLog);

        // Assert
        verify(changeLogRepository).save(isA(ChangeLogEntity.class));
        HttpStatusCode statusCode = actualCreateChangeLogResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualCreateChangeLogResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualCreateChangeLogResult.hasBody());
        assertTrue(actualCreateChangeLogResult.getHeaders().isEmpty());
        assertSame(changeLogEntity, actualCreateChangeLogResult.getBody());
    }
}
