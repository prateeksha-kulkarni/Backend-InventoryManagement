package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    private StoreService storeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        storeService = new StoreService(storeRepository);
    }

    @Test
    public void testGetAllStores() {
        // Arrange
        List<StoreEntity> expectedStores = new ArrayList<>();
        StoreEntity store1 = mock(StoreEntity.class);
        StoreEntity store2 = mock(StoreEntity.class);
        expectedStores.add(store1);
        expectedStores.add(store2);
        doReturn(expectedStores).when(storeRepository).findAll();
        // Act
        List<StoreEntity> result = storeService.getAllStores();
        // Assert
        assertThat(result, is(sameInstance(expectedStores)));
        verify(storeRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAllStoresEmptyList() {
        // Arrange
        List<StoreEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(storeRepository).findAll();
        // Act
        List<StoreEntity> result = storeService.getAllStores();
        // Assert
        assertThat(result, is(sameInstance(emptyList)));
        verify(storeRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetStoreByIdFound() {
        // Arrange
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        StoreEntity expectedStore = mock(StoreEntity.class);
        Optional<StoreEntity> optionalStore = Optional.of(expectedStore);
        doReturn(optionalStore).when(storeRepository).findById(storeId);
        // Act
        StoreEntity result = storeService.getStoreById(storeId);
        // Assert
        assertThat(result, is(sameInstance(expectedStore)));
        verify(storeRepository, atLeast(1)).findById(storeId);
    }

    @Test
    public void testGetStoreByIdNotFound() {
        // Arrange
        UUID storeId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Optional<StoreEntity> emptyOptional = Optional.empty();
        doReturn(emptyOptional).when(storeRepository).findById(storeId);
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            storeService.getStoreById(storeId);
        });
        assertEquals("Store not found", exception.getMessage());
        verify(storeRepository, atLeast(1)).findById(storeId);
    }

    @Test
    public void testCreateStore() {
        // Arrange
        StoreEntity inputStore = mock(StoreEntity.class);
        StoreEntity savedStore = mock(StoreEntity.class);
        doReturn(savedStore).when(storeRepository).save(inputStore);
        // Act
        StoreEntity result = storeService.createStore(inputStore);
        // Assert
        assertThat(result, is(sameInstance(savedStore)));
        verify(storeRepository, atLeast(1)).save(inputStore);
    }

    @Test
    public void testStoreServiceInstantiation() {
        // Act
        StoreService service = new StoreService(storeRepository);
        // Assert
        assertThat(service, is(notNullValue()));
    }
}
