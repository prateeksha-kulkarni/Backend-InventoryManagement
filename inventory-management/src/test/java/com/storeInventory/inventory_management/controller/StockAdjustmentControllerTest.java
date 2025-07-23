package com.storeInventory.inventory_management.controller;

import com.storeInventory.inventory_management.auth.controller.StockAdjustmentController;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.service.StockAdjustmentService;
import com.storeInventory.inventory_management.auth.dto.response.StockAdjustmentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import org.mockito.MockedStatic;

@Timeout(10)
public class StockAdjustmentControllerTest {

    @Mock
    private StockAdjustmentService stockAdjustmentService;

    private StockAdjustmentController stockAdjustmentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockAdjustmentController = new StockAdjustmentController(stockAdjustmentService);
    }

    @Test
    public void testGetAllAdjustments() {
        StockAdjustmentEntity entity1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity entity2 = mock(StockAdjustmentEntity.class);
        List<StockAdjustmentEntity> entities = Arrays.asList(entity1, entity2);
        StockAdjustmentResponseDto dto1 = mock(StockAdjustmentResponseDto.class);
        StockAdjustmentResponseDto dto2 = mock(StockAdjustmentResponseDto.class);
        when(stockAdjustmentService.getAllAdjustments()).thenReturn(entities);
        try (MockedStatic<StockAdjustmentResponseDto> mockedStatic = mockStatic(StockAdjustmentResponseDto.class)) {
            mockedStatic.when(() -> StockAdjustmentResponseDto.fromEntity(entity1)).thenReturn(dto1);
            mockedStatic.when(() -> StockAdjustmentResponseDto.fromEntity(entity2)).thenReturn(dto2);
            StockAdjustmentController spyController = spy(stockAdjustmentController);
            ResponseEntity<List<StockAdjustmentResponseDto>> result = spyController.getAllAdjustments();
            assertNotNull(result);
            assertEquals(200, result.getStatusCode().value());
            assertNotNull(result.getBody());
            assertEquals(2, result.getBody().size());
            verify(stockAdjustmentService, atLeast(1)).getAllAdjustments();
        }
    }

    @Test
    public void testGetAllAdjustmentsEmptyList() {
        List<StockAdjustmentEntity> emptyEntities = Arrays.asList();
        when(stockAdjustmentService.getAllAdjustments()).thenReturn(emptyEntities);
        StockAdjustmentController spyController = spy(stockAdjustmentController);
        ResponseEntity<List<StockAdjustmentResponseDto>> result = spyController.getAllAdjustments();
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());
        verify(stockAdjustmentService, atLeast(1)).getAllAdjustments();
    }

    @Test
    public void testGetAdjustmentsByInventory() {
        UUID inventoryId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        StockAdjustmentEntity entity1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity entity2 = mock(StockAdjustmentEntity.class);
        List<StockAdjustmentEntity> entities = Arrays.asList(entity1, entity2);
        when(stockAdjustmentService.getAdjustmentsByInventory(eq(inventoryId))).thenReturn(entities);
        StockAdjustmentController spyController = spy(stockAdjustmentController);
        ResponseEntity<List<StockAdjustmentEntity>> result = spyController.getAdjustmentsByInventory(inventoryId);
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        verify(stockAdjustmentService, atLeast(1)).getAdjustmentsByInventory(eq(inventoryId));
    }

    @Test
    public void testGetAdjustmentsByInventoryEmptyList() {
        UUID inventoryId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        List<StockAdjustmentEntity> emptyEntities = Arrays.asList();
        when(stockAdjustmentService.getAdjustmentsByInventory(eq(inventoryId))).thenReturn(emptyEntities);
        StockAdjustmentController spyController = spy(stockAdjustmentController);
        ResponseEntity<List<StockAdjustmentEntity>> result = spyController.getAdjustmentsByInventory(inventoryId);
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());
        verify(stockAdjustmentService, atLeast(1)).getAdjustmentsByInventory(eq(inventoryId));
    }

    @Test
    public void testCreateAdjustment() {
        StockAdjustmentEntity inputAdjustment = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity createdAdjustment = mock(StockAdjustmentEntity.class);
        when(stockAdjustmentService.createAdjustment(eq(inputAdjustment))).thenReturn(createdAdjustment);
        StockAdjustmentController spyController = spy(stockAdjustmentController);
        ResponseEntity<StockAdjustmentEntity> result = spyController.createAdjustment(inputAdjustment);
        assertNotNull(result);
        assertEquals(200, result.getStatusCode().value());
        assertNotNull(result.getBody());
        assertThat(result.getBody(), is(createdAdjustment));
        verify(stockAdjustmentService, atLeast(1)).createAdjustment(eq(inputAdjustment));
    }

    @Test
    public void testControllerInstantiation() {
        StockAdjustmentController controller = new StockAdjustmentController(stockAdjustmentService);
        assertThat(controller, is(notNullValue()));
    }
}
