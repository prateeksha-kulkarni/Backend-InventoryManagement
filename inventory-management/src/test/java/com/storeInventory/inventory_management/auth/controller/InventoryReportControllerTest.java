package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.service.InventoryReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class InventoryReportControllerTest {

    private InventoryReportService inventoryReportService;

    private InventoryReportController inventoryReportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryReportService = mock(InventoryReportService.class);
        inventoryReportController = new InventoryReportController(inventoryReportService);
    }

    @Test
    public void testGetTurnoverReportWithValidRequest() {
        // Arrange
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 12, 31));
        InventoryTurnoverResponse response1 = new InventoryTurnoverResponse();
        response1.setStoreId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        response1.setStoreName("Store A");
        response1.setProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
        response1.setProductName("Product A");
        response1.setQuantity(100);
        response1.setMinThreshold(10);
        response1.setStockStatus("Healthy");
        response1.setTurnoverRate(0.5);
        InventoryTurnoverResponse response2 = new InventoryTurnoverResponse();
        response2.setStoreId(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"));
        response2.setStoreName("Store B");
        response2.setProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"));
        response2.setProductName("Product B");
        response2.setQuantity(50);
        response2.setMinThreshold(5);
        response2.setStockStatus("Overstock");
        response2.setTurnoverRate(0.8);
        List<InventoryTurnoverResponse> expectedResponses = new ArrayList<>();
        expectedResponses.add(response1);
        expectedResponses.add(response2);
        doReturn(expectedResponses).when(inventoryReportService).calculateTurnover(any(InventoryTurnoverRequest.class));
        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> result = inventoryReportController.getTurnoverReport(request);
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().size());
        InventoryTurnoverResponse actualResponse1 = result.getBody().get(0);
        assertThat(actualResponse1.getStoreId(), equalTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440000")));
        assertThat(actualResponse1.getStoreName(), equalTo("Store A"));
        assertThat(actualResponse1.getProductId(), equalTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440001")));
        assertThat(actualResponse1.getProductName(), equalTo("Product A"));
        assertThat(actualResponse1.getQuantity(), equalTo(100));
        assertThat(actualResponse1.getMinThreshold(), equalTo(10));
        assertThat(actualResponse1.getStockStatus(), equalTo("Healthy"));
        assertThat(actualResponse1.getTurnoverRate(), equalTo(0.5));
        InventoryTurnoverResponse actualResponse2 = result.getBody().get(1);
        assertThat(actualResponse2.getStoreId(), equalTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440002")));
        assertThat(actualResponse2.getStoreName(), equalTo("Store B"));
        assertThat(actualResponse2.getProductId(), equalTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440003")));
        assertThat(actualResponse2.getProductName(), equalTo("Product B"));
        assertThat(actualResponse2.getQuantity(), equalTo(50));
        assertThat(actualResponse2.getMinThreshold(), equalTo(5));
        assertThat(actualResponse2.getStockStatus(), equalTo("Overstock"));
        assertThat(actualResponse2.getTurnoverRate(), equalTo(0.8));
        verify(inventoryReportService, atLeast(1)).calculateTurnover(any(InventoryTurnoverRequest.class));
    }

    @Test
    public void testGetTurnoverReportWithEmptyList() {
        // Arrange
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 1, 1));
        request.setEndDate(LocalDate.of(2023, 12, 31));
        List<InventoryTurnoverResponse> emptyList = new ArrayList<>();
        doReturn(emptyList).when(inventoryReportService).calculateTurnover(any(InventoryTurnoverRequest.class));
        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> result = inventoryReportController.getTurnoverReport(request);
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(0, result.getBody().size());
        verify(inventoryReportService, atLeast(1)).calculateTurnover(any(InventoryTurnoverRequest.class));
    }

    @Test
    public void testGetTurnoverReportWithSingleItem() {
        // Arrange
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.of(2023, 6, 1));
        request.setEndDate(LocalDate.of(2023, 6, 30));
        InventoryTurnoverResponse singleResponse = new InventoryTurnoverResponse();
        singleResponse.setStoreId(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"));
        singleResponse.setStoreName("Store C");
        singleResponse.setProductId(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"));
        singleResponse.setProductName("Product C");
        singleResponse.setQuantity(5);
        singleResponse.setMinThreshold(10);
        singleResponse.setStockStatus("Stockout");
        singleResponse.setTurnoverRate(0.0);
        List<InventoryTurnoverResponse> singleItemList = new ArrayList<>();
        singleItemList.add(singleResponse);
        doReturn(singleItemList).when(inventoryReportService).calculateTurnover(any(InventoryTurnoverRequest.class));
        // Act
        ResponseEntity<List<InventoryTurnoverResponse>> result = inventoryReportController.getTurnoverReport(request);
        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        InventoryTurnoverResponse actualResponse = result.getBody().get(0);
        assertThat(actualResponse.getStoreId(), equalTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440004")));
        assertThat(actualResponse.getStoreName(), equalTo("Store C"));
        assertThat(actualResponse.getProductId(), equalTo(UUID.fromString("550e8400-e29b-41d4-a716-446655440005")));
        assertThat(actualResponse.getProductName(), equalTo("Product C"));
        assertThat(actualResponse.getQuantity(), equalTo(5));
        assertThat(actualResponse.getMinThreshold(), equalTo(10));
        assertThat(actualResponse.getStockStatus(), equalTo("Stockout"));
        assertThat(actualResponse.getTurnoverRate(), equalTo(0.0));
        verify(inventoryReportService, atLeast(1)).calculateTurnover(any(InventoryTurnoverRequest.class));
    }

    @Test
    public void testConstructorInitialization() {
        // Arrange
        InventoryReportService mockService = mock(InventoryReportService.class);
        // Act
        InventoryReportController controller = new InventoryReportController(mockService);
        // Assert
        assertThat(controller, notNullValue());
    }
}
