package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.response.StockAdjustmentResponseDto;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.service.StockAdjustmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class StockAdjustmentControllerTest {

    @Mock
    private StockAdjustmentService stockAdjustmentService;

    private StockAdjustmentController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new StockAdjustmentController(stockAdjustmentService);
    }

    @Test
    public void testGetAllAdjustments() {
        // Arrange
        StockAdjustmentEntity entity1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity entity2 = mock(StockAdjustmentEntity.class);
        List<StockAdjustmentEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        StockAdjustmentResponseDto dto1 = mock(StockAdjustmentResponseDto.class);
        StockAdjustmentResponseDto dto2 = mock(StockAdjustmentResponseDto.class);
        doReturn(entities).when(stockAdjustmentService).getAllAdjustments();
        try (MockedStatic<StockAdjustmentResponseDto> mockedDto = mockStatic(StockAdjustmentResponseDto.class)) {
            mockedDto.when(() -> StockAdjustmentResponseDto.fromEntity(entity1)).thenReturn(dto1);
            mockedDto.when(() -> StockAdjustmentResponseDto.fromEntity(entity2)).thenReturn(dto2);
            // Act
            ResponseEntity<List<StockAdjustmentResponseDto>> result = controller.getAllAdjustments();
            // Assert
            assertThat(result, is(notNullValue()));
            assertEquals(200, result.getStatusCodeValue());
            assertThat(result.getBody(), is(notNullValue()));
            assertEquals(2, result.getBody().size());
            assertEquals(dto1, result.getBody().get(0));
            assertEquals(dto2, result.getBody().get(1));
            verify(stockAdjustmentService, atLeast(1)).getAllAdjustments();
            mockedDto.verify(() -> StockAdjustmentResponseDto.fromEntity(entity1), atLeast(1));
            mockedDto.verify(() -> StockAdjustmentResponseDto.fromEntity(entity2), atLeast(1));
        }
    }

    @Test
    public void testGetAllAdjustmentsEmptyList() {
        // Arrange
        List<StockAdjustmentEntity> entities = new ArrayList<>();
        doReturn(entities).when(stockAdjustmentService).getAllAdjustments();
        // Act
        ResponseEntity<List<StockAdjustmentResponseDto>> result = controller.getAllAdjustments();
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertEquals(0, result.getBody().size());
        verify(stockAdjustmentService, atLeast(1)).getAllAdjustments();
    }

    @Test
    public void testGetAdjustmentsByInventory() {
        // Arrange
        UUID inventoryId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        StockAdjustmentEntity entity1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity entity2 = mock(StockAdjustmentEntity.class);
        List<StockAdjustmentEntity> entities = new ArrayList<>();
        entities.add(entity1);
        entities.add(entity2);
        doReturn(entities).when(stockAdjustmentService).getAdjustmentsByInventory(inventoryId);
        // Act
        ResponseEntity<List<StockAdjustmentEntity>> result = controller.getAdjustmentsByInventory(inventoryId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertEquals(2, result.getBody().size());
        assertEquals(entity1, result.getBody().get(0));
        assertEquals(entity2, result.getBody().get(1));
        verify(stockAdjustmentService, atLeast(1)).getAdjustmentsByInventory(inventoryId);
    }

    @Test
    public void testGetAdjustmentsByInventoryEmptyList() {
        // Arrange
        UUID inventoryId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        List<StockAdjustmentEntity> entities = new ArrayList<>();
        doReturn(entities).when(stockAdjustmentService).getAdjustmentsByInventory(inventoryId);
        // Act
        ResponseEntity<List<StockAdjustmentEntity>> result = controller.getAdjustmentsByInventory(inventoryId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertEquals(0, result.getBody().size());
        verify(stockAdjustmentService, atLeast(1)).getAdjustmentsByInventory(inventoryId);
    }

    @Test
    public void testCreateAdjustment() {
        // Arrange
        StockAdjustmentEntity inputAdjustment = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity createdAdjustment = mock(StockAdjustmentEntity.class);
        doReturn(createdAdjustment).when(stockAdjustmentService).createAdjustment(inputAdjustment);
        // Act
        ResponseEntity<StockAdjustmentEntity> result = controller.createAdjustment(inputAdjustment);
        // Assert
        assertThat(result, is(notNullValue()));
        assertEquals(200, result.getStatusCodeValue());
        assertThat(result.getBody(), is(notNullValue()));
        assertEquals(createdAdjustment, result.getBody());
        verify(stockAdjustmentService, atLeast(1)).createAdjustment(inputAdjustment);
    }
}
