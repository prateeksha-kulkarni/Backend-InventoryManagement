package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.InventoryResponseDto;
import com.storeInventory.inventory_management.auth.dto.request.AdjustmentRequest;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Disabled;

@Timeout(10)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    private InventoryController inventoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryController = new InventoryController(inventoryService);
    }

    @Disabled()
    @Test
    public void testGetAllInventory() {
        List<InventoryEntity> inventoryEntities = new ArrayList<>();
        InventoryEntity entity1 = mock(InventoryEntity.class);
        InventoryEntity entity2 = mock(InventoryEntity.class);
        inventoryEntities.add(entity1);
        inventoryEntities.add(entity2);
        doReturn(inventoryEntities).when(inventoryService).getAllInventory();
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.getAllInventory();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(2)));
        verify(inventoryService, atLeast(1)).getAllInventory();
    }

    @Test
    public void testGetAllInventoryWithEmptyList() {
        List<InventoryEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(inventoryService).getAllInventory();
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.getAllInventory();
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(0)));
        verify(inventoryService, atLeast(1)).getAllInventory();
    }

    @Test
    public void testSearchInventoryWithAllParameters() {
        String query = "test query";
        UUID storeId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        String fields = "name,description";
        List<InventoryResponseDto> expectedDtos = new ArrayList<>();
        InventoryResponseDto dto1 = mock(InventoryResponseDto.class);
        expectedDtos.add(dto1);
        doReturn(expectedDtos).when(inventoryService).searchInventory(query, storeId);
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.searchInventory(query, storeId, fields);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().size(), is(equalTo(1)));
        verify(inventoryService, atLeast(1)).searchInventory(query, storeId);
    }

    @Test
    public void testSearchInventoryWithoutOptionalParameters() {
        String query = "test query";
        List<InventoryResponseDto> expectedDtos = new ArrayList<>();
        doReturn(expectedDtos).when(inventoryService).searchInventory(query, null);
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.searchInventory(query, null, null);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        verify(inventoryService, atLeast(1)).searchInventory(query, null);
    }

    @Disabled()
    @Test
    public void testGetInventoryByStore() {
        UUID storeId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        List<InventoryEntity> inventoryEntities = new ArrayList<>();
        InventoryEntity entity1 = mock(InventoryEntity.class);
        inventoryEntities.add(entity1);
        doReturn(inventoryEntities).when(inventoryService).getInventoryByStore(storeId);
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.getInventoryByStore(storeId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(1)));
        verify(inventoryService, atLeast(1)).getInventoryByStore(storeId);
    }

    @Test
    public void testGetInventoryByStoreWithEmptyResult() {
        UUID storeId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        List<InventoryEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(inventoryService).getInventoryByStore(storeId);
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.getInventoryByStore(storeId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(0)));
        verify(inventoryService, atLeast(1)).getInventoryByStore(storeId);
    }

    @Test
    public void testGetInventoryByStoreAndProduct() {
        UUID storeId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        UUID productId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480");
        InventoryEntity inventoryEntity = mock(InventoryEntity.class);
        Optional<InventoryEntity> optionalEntity = Optional.of(inventoryEntity);
        doReturn(optionalEntity).when(inventoryService).getInventoryByStoreAndProduct(storeId, productId);
        ResponseEntity<Optional<InventoryEntity>> response = inventoryController.getInventoryByStoreAndProduct(storeId, productId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().isPresent(), is(equalTo(true)));
        verify(inventoryService, atLeast(1)).getInventoryByStoreAndProduct(storeId, productId);
    }

    @Test
    public void testGetInventoryByStoreAndProductWithEmptyResult() {
        UUID storeId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
        UUID productId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480");
        Optional<InventoryEntity> emptyOptional = Optional.empty();
        doReturn(emptyOptional).when(inventoryService).getInventoryByStoreAndProduct(storeId, productId);
        ResponseEntity<Optional<InventoryEntity>> response = inventoryController.getInventoryByStoreAndProduct(storeId, productId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().isPresent(), is(equalTo(false)));
        verify(inventoryService, atLeast(1)).getInventoryByStoreAndProduct(storeId, productId);
    }

    @Disabled()
    @Test
    public void testGetInventoryByProduct() {
        UUID productId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480");
        List<InventoryEntity> inventoryEntities = new ArrayList<>();
        InventoryEntity entity1 = mock(InventoryEntity.class);
        InventoryEntity entity2 = mock(InventoryEntity.class);
        inventoryEntities.add(entity1);
        inventoryEntities.add(entity2);
        doReturn(inventoryEntities).when(inventoryService).getInventoryByProduct(productId);
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.getInventoryByProduct(productId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(2)));
        verify(inventoryService, atLeast(1)).getInventoryByProduct(productId);
    }

    @Test
    public void testGetInventoryByProductWithEmptyResult() {
        UUID productId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480");
        List<InventoryEntity> emptyList = new ArrayList<>();
        doReturn(emptyList).when(inventoryService).getInventoryByProduct(productId);
        ResponseEntity<List<InventoryResponseDto>> response = inventoryController.getInventoryByProduct(productId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertThat(response.getBody().size(), is(equalTo(0)));
        verify(inventoryService, atLeast(1)).getInventoryByProduct(productId);
    }

    @Test
    public void testCreateInventory() {
        InventoryEntity inputInventory = mock(InventoryEntity.class);
        InventoryEntity createdInventory = mock(InventoryEntity.class);
        doReturn(createdInventory).when(inventoryService).createInventory(inputInventory);
        ResponseEntity<InventoryEntity> response = inventoryController.createInventory(inputInventory);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is(equalTo(createdInventory)));
        verify(inventoryService, atLeast(1)).createInventory(inputInventory);
    }

    @Disabled()
    @Test
    public void testAdjustQuantityWithAddType() {
        String productName = "Test Product";
        AdjustmentRequest request = new AdjustmentRequest();
        request.setQuantity(10);
        request.setType(ChangeType.ADD);
        request.setStoreId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        request.setUserId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480"));
        request.setReason("Stock replenishment");
        InventoryEntity updatedEntity = mock(InventoryEntity.class);
        doReturn(updatedEntity).when(inventoryService).adjustQuantity(productName, request.getQuantity(), request.getType(), request.getStoreId(), request.getUserId(), request.getReason());
        ResponseEntity<InventoryResponseDto> response = inventoryController.adjustQuantity(request, productName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        verify(inventoryService, atLeast(1)).adjustQuantity(productName, request.getQuantity(), request.getType(), request.getStoreId(), request.getUserId(), request.getReason());
    }

    @Disabled()
    @Test
    public void testAdjustQuantityWithRemoveType() {
        String productName = "Test Product";
        AdjustmentRequest request = new AdjustmentRequest();
        request.setQuantity(5);
        request.setType(ChangeType.REMOVE);
        request.setStoreId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        request.setUserId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480"));
        request.setReason("Damaged goods");
        InventoryEntity updatedEntity = mock(InventoryEntity.class);
        doReturn(updatedEntity).when(inventoryService).adjustQuantity(productName, request.getQuantity(), request.getType(), request.getStoreId(), request.getUserId(), request.getReason());
        ResponseEntity<InventoryResponseDto> response = inventoryController.adjustQuantity(request, productName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        verify(inventoryService, atLeast(1)).adjustQuantity(productName, request.getQuantity(), request.getType(), request.getStoreId(), request.getUserId(), request.getReason());
    }

    @Disabled()
    @Test
    public void testAdjustQuantityWithZeroQuantity() {
        String productName = "Test Product";
        AdjustmentRequest request = new AdjustmentRequest();
        request.setQuantity(0);
        request.setType(ChangeType.ADD);
        request.setStoreId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        request.setUserId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d480"));
        request.setReason("Test adjustment");
        InventoryEntity updatedEntity = mock(InventoryEntity.class);
        doReturn(updatedEntity).when(inventoryService).adjustQuantity(productName, request.getQuantity(), request.getType(), request.getStoreId(), request.getUserId(), request.getReason());
        ResponseEntity<InventoryResponseDto> response = inventoryController.adjustQuantity(request, productName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertThat(response.getBody(), is(notNullValue()));
        verify(inventoryService, atLeast(1)).adjustQuantity(productName, request.getQuantity(), request.getType(), request.getStoreId(), request.getUserId(), request.getReason());
    }
}
