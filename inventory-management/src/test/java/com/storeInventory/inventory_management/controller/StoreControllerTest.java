package com.storeInventory.inventory_management.controller;

import com.storeInventory.inventory_management.auth.controller.StoreController;
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
    void setUp() {
        MockitoAnnotations.openMocks(this);
        storeController = new StoreController(storeService);
    }

    @Test
    void testGetAllStores() {
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
        ResponseEntity<List<StoreEntity>> result = storeController.getAllStores();
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStores, result.getBody());
        verify(storeService, atLeast(1)).getAllStores();
    }

    @Test
    void testGetAllStoresWithEmptyList() {
        List<StoreEntity> emptyStores = new ArrayList<>();
        doReturn(emptyStores).when(storeService).getAllStores();
        ResponseEntity<List<StoreEntity>> result = storeController.getAllStores();
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(emptyStores, result.getBody());
        verify(storeService, atLeast(1)).getAllStores();
    }

    @Test
    void testGetStoreById() {
        UUID storeId = UUID.fromString("12345678-1234-1234-1234-123456789012");
        StoreEntity expectedStore = new StoreEntity();
        expectedStore.setStoreId(storeId);
        expectedStore.setName("Test Store");
        expectedStore.setLocation("Test Location");
        doReturn(expectedStore).when(storeService).getStoreById(storeId);
        ResponseEntity<StoreEntity> result = storeController.getStoreById(storeId);
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStore, result.getBody());
        verify(storeService, atLeast(1)).getStoreById(storeId);
    }

    @Test
    void testGetStoreByIdWithDifferentUuid() {
        UUID storeId = UUID.fromString("87654321-4321-4321-4321-210987654321");
        StoreEntity expectedStore = new StoreEntity();
        expectedStore.setStoreId(storeId);
        expectedStore.setName("Another Store");
        expectedStore.setLocation("Another Location");
        doReturn(expectedStore).when(storeService).getStoreById(storeId);
        ResponseEntity<StoreEntity> result = storeController.getStoreById(storeId);
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStore, result.getBody());
        verify(storeService, atLeast(1)).getStoreById(storeId);
    }

    @Test
    void testCreateStore() {
        StoreEntity inputStore = new StoreEntity();
        inputStore.setName("New Store");
        inputStore.setLocation("New Location");
        StoreEntity expectedStore = new StoreEntity();
        expectedStore.setStoreId(UUID.randomUUID());
        expectedStore.setName("New Store");
        expectedStore.setLocation("New Location");
        doReturn(expectedStore).when(storeService).createStore(inputStore);
        ResponseEntity<StoreEntity> result = storeController.createStore(inputStore);
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStore, result.getBody());
        verify(storeService, atLeast(1)).createStore(inputStore);
    }

    @Test
    void testCreateStoreWithCompleteData() {
        StoreEntity inputStore = new StoreEntity();
        inputStore.setName("Complete Store");
        inputStore.setLocation("Complete Location");
        StoreEntity expectedStore = new StoreEntity();
        expectedStore.setStoreId(UUID.fromString("11111111-2222-3333-4444-555555555555"));
        expectedStore.setName("Complete Store");
        expectedStore.setLocation("Complete Location");
        doReturn(expectedStore).when(storeService).createStore(inputStore);
        ResponseEntity<StoreEntity> result = storeController.createStore(inputStore);
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(expectedStore, result.getBody());
        verify(storeService, atLeast(1)).createStore(inputStore);
    }
}
