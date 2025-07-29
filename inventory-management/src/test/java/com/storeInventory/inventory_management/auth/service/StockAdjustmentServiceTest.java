package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class StockAdjustmentServiceTest {

    @Mock
    private StockAdjustmentRepository stockAdjustmentRepository;

    private StockAdjustmentService stockAdjustmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        stockAdjustmentService = new StockAdjustmentService(stockAdjustmentRepository);
    }

    @Test
    public void testGetAllAdjustments() {
        // Arrange
        List<StockAdjustmentEntity> expectedAdjustments = new ArrayList<>();
        StockAdjustmentEntity adjustment1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity adjustment2 = mock(StockAdjustmentEntity.class);
        expectedAdjustments.add(adjustment1);
        expectedAdjustments.add(adjustment2);
        doReturn(expectedAdjustments).when(stockAdjustmentRepository).findAll();
        // Act
        List<StockAdjustmentEntity> result = stockAdjustmentService.getAllAdjustments();
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertEquals(expectedAdjustments, result);
        verify(stockAdjustmentRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAllAdjustmentsEmptyList() {
        // Arrange
        List<StockAdjustmentEntity> expectedAdjustments = new ArrayList<>();
        doReturn(expectedAdjustments).when(stockAdjustmentRepository).findAll();
        // Act
        List<StockAdjustmentEntity> result = stockAdjustmentService.getAllAdjustments();
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        assertEquals(expectedAdjustments, result);
        verify(stockAdjustmentRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAdjustmentsByInventoryWithMatchingAdjustments() {
        // Arrange
        UUID targetInventoryId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID otherInventoryId = UUID.fromString("987fcdeb-51a2-43d7-8f9e-123456789abc");
        InventoryEntity targetInventory = mock(InventoryEntity.class);
        InventoryEntity otherInventory = mock(InventoryEntity.class);
        doReturn(targetInventoryId).when(targetInventory).getInventoryId();
        doReturn(otherInventoryId).when(otherInventory).getInventoryId();
        StockAdjustmentEntity matchingAdjustment1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity matchingAdjustment2 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity nonMatchingAdjustment = mock(StockAdjustmentEntity.class);
        doReturn(targetInventory).when(matchingAdjustment1).getInventory();
        doReturn(targetInventory).when(matchingAdjustment2).getInventory();
        doReturn(otherInventory).when(nonMatchingAdjustment).getInventory();
        List<StockAdjustmentEntity> allAdjustments = new ArrayList<>();
        allAdjustments.add(matchingAdjustment1);
        allAdjustments.add(nonMatchingAdjustment);
        allAdjustments.add(matchingAdjustment2);
        doReturn(allAdjustments).when(stockAdjustmentRepository).findAll();
        // Act
        List<StockAdjustmentEntity> result = stockAdjustmentService.getAdjustmentsByInventory(targetInventoryId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(2));
        assertThat(result.get(0), equalTo(matchingAdjustment1));
        assertThat(result.get(1), equalTo(matchingAdjustment2));
        verify(stockAdjustmentRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAdjustmentsByInventoryWithNoMatchingAdjustments() {
        // Arrange
        UUID targetInventoryId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID otherInventoryId = UUID.fromString("987fcdeb-51a2-43d7-8f9e-123456789abc");
        InventoryEntity otherInventory = mock(InventoryEntity.class);
        doReturn(otherInventoryId).when(otherInventory).getInventoryId();
        StockAdjustmentEntity nonMatchingAdjustment1 = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity nonMatchingAdjustment2 = mock(StockAdjustmentEntity.class);
        doReturn(otherInventory).when(nonMatchingAdjustment1).getInventory();
        doReturn(otherInventory).when(nonMatchingAdjustment2).getInventory();
        List<StockAdjustmentEntity> allAdjustments = new ArrayList<>();
        allAdjustments.add(nonMatchingAdjustment1);
        allAdjustments.add(nonMatchingAdjustment2);
        doReturn(allAdjustments).when(stockAdjustmentRepository).findAll();
        // Act
        List<StockAdjustmentEntity> result = stockAdjustmentService.getAdjustmentsByInventory(targetInventoryId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(stockAdjustmentRepository, atLeast(1)).findAll();
    }

    @Test
    public void testGetAdjustmentsByInventoryWithEmptyList() {
        // Arrange
        UUID targetInventoryId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        List<StockAdjustmentEntity> allAdjustments = new ArrayList<>();
        doReturn(allAdjustments).when(stockAdjustmentRepository).findAll();
        // Act
        List<StockAdjustmentEntity> result = stockAdjustmentService.getAdjustmentsByInventory(targetInventoryId);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, hasSize(0));
        verify(stockAdjustmentRepository, atLeast(1)).findAll();
    }

    @Test
    public void testCreateAdjustment() {
        // Arrange
        StockAdjustmentEntity inputAdjustment = mock(StockAdjustmentEntity.class);
        StockAdjustmentEntity savedAdjustment = mock(StockAdjustmentEntity.class);
        doReturn(savedAdjustment).when(stockAdjustmentRepository).save(any(StockAdjustmentEntity.class));
        // Act
        StockAdjustmentEntity result = stockAdjustmentService.createAdjustment(inputAdjustment);
        // Assert
        assertThat(result, is(notNullValue()));
        assertThat(result, equalTo(savedAdjustment));
        verify(stockAdjustmentRepository, atLeast(1)).save(inputAdjustment);
    }
}
