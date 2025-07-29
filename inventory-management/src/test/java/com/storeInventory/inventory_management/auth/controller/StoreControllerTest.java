package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class StoreControllerTest {

    @Mock
    private StoreService storeService;

    private StoreController storeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        storeController = new StoreController(storeService);
    }

    @Test
    public void testGetAllStoresReturnsListOfStores() {
        // Arrange
        List<StoreEntity> expectedStores = new ArrayList<>();
        StoreEntity store1 = new StoreEntity();
        store1.setStoreId(UUID.randomUUID());
        store1.setName("Store 1");
        store1.setLocation("Location 1");
        expectedStores.add(store1);
        StoreEntity store2 = new StoreEntity();
        store2.setStoreId(UUID.randomUUID());
        store2.setName("Store 2");
        store2.setLocation("Location 2");
        expectedStores.add(store2);
        doReturn(expectedStores).when(storeService).getAllStores();
        // Act
        ResponseEntity<List<StoreEntity>> result = storeController.getAllStores();
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStores, result.getBody());
        verify(storeService, atLeast(1)).getAllStores();
    }

    @Test
    public void testGetAllStoresReturnsEmptyList() {
        // Arrange
        List<StoreEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(storeService).getAllStores();
        // Act
        ResponseEntity<List<StoreEntity>> result = storeController.getAllStores();
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(emptyList, result.getBody());
        verify(storeService, atLeast(1)).getAllStores();
    }

    @Test
    public void testGetStoreByIdReturnsStore() {
        // Arrange
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        StoreEntity expectedStore = new StoreEntity();
        expectedStore.setStoreId(storeId);
        expectedStore.setName("Test Store");
        expectedStore.setLocation("Test Location");
        doReturn(expectedStore).when(storeService).getStoreById(storeId);
        // Act
        ResponseEntity<StoreEntity> result = storeController.getStoreById(storeId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStore, result.getBody());
        verify(storeService, atLeast(1)).getStoreById(storeId);
    }

    @Test
    public void testGetStoreByIdWithDifferentUuid() {
        // Arrange
        UUID storeId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        StoreEntity expectedStore = new StoreEntity();
        expectedStore.setStoreId(storeId);
        expectedStore.setName("Another Store");
        expectedStore.setLocation("Another Location");
        doReturn(expectedStore).when(storeService).getStoreById(storeId);
        // Act
        ResponseEntity<StoreEntity> result = storeController.getStoreById(storeId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStore, result.getBody());
        verify(storeService, atLeast(1)).getStoreById(storeId);
    }

    @Test
    public void testCreateStoreReturnsCreatedStore() {
        // Arrange
        StoreEntity inputStore = new StoreEntity();
        inputStore.setName("New Store");
        inputStore.setLocation("New Location");
        StoreEntity createdStore = new StoreEntity();
        createdStore.setStoreId(UUID.fromString("987fcdeb-51a2-43d1-9c4f-123456789abc"));
        createdStore.setName("New Store");
        createdStore.setLocation("New Location");
        doReturn(createdStore).when(storeService).createStore(inputStore);
        // Act
        ResponseEntity<StoreEntity> result = storeController.createStore(inputStore);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(createdStore, result.getBody());
        verify(storeService, atLeast(1)).createStore(inputStore);
    }

    @Test
    public void testCreateStoreWithDifferentStoreData() {
        // Arrange
        StoreEntity inputStore = new StoreEntity();
        inputStore.setName("Different Store");
        inputStore.setLocation("Different Location");
        StoreEntity createdStore = new StoreEntity();
        createdStore.setStoreId(UUID.fromString("abcdef12-3456-7890-abcd-ef1234567890"));
        createdStore.setName("Different Store");
        createdStore.setLocation("Different Location");
        doReturn(createdStore).when(storeService).createStore(inputStore);
        // Act
        ResponseEntity<StoreEntity> result = storeController.createStore(inputStore);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(createdStore, result.getBody());
        verify(storeService, atLeast(1)).createStore(inputStore);
    }
}
