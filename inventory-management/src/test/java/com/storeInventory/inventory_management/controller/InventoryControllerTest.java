package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.InventoryController;
import com.storeInventory.inventory_management.auth.dto.InventoryResponseDto;
import com.storeInventory.inventory_management.auth.dto.request.AdjustmentRequest;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = InventoryController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class InventoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryEntity getSampleInventory() {
        InventoryEntity inventory = new InventoryEntity();
        inventory.setInventoryId(UUID.randomUUID());
        inventory.setQuantity(100);
        inventory.setMinThreshold(10);
        return inventory;
    }

    private InventoryResponseDto getSampleResponseDto(InventoryEntity inventory) {
        InventoryResponseDto dto = new InventoryResponseDto();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setQuantity(inventory.getQuantity());
        dto.setMinThreshold(inventory.getMinThreshold());
        return dto;
    }

    @Test
    void testGetAllInventory() throws Exception {
        InventoryEntity inventory = getSampleInventory();
        when(inventoryService.getAllInventory()).thenReturn(Collections.singletonList(inventory));
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$0.quantity", is(100)));
    }

    @Test
    void testSearchInventory() throws Exception {
        InventoryResponseDto dto = getSampleResponseDto(getSampleInventory());
        when(inventoryService.searchInventory(anyString(), any(UUID.class))).thenReturn(Collections.singletonList(dto));
        mockMvc.perform(get("/api/inventory/search?query=test&storeId=" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$0.quantity", is(100)));
    }

    @Test
    void testGetInventoryByStore() throws Exception {
        UUID storeId = UUID.randomUUID();
        InventoryEntity inventory = getSampleInventory();
        when(inventoryService.getInventoryByStore(eq(storeId))).thenReturn(Collections.singletonList(inventory));
        mockMvc.perform(get("/api/inventory/store/" + storeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$0.quantity", is(100)));
    }

    @Test
    void testGetInventoryByStoreAndProduct() throws Exception {
        UUID storeId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        InventoryEntity inventory = getSampleInventory();
        when(inventoryService.getInventoryByStoreAndProduct(eq(storeId), eq(productId))).thenReturn(Optional.of(inventory));
        mockMvc.perform(get("/api/inventory/store/" + storeId + "/product/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(100)));
    }

    @Test
    void testGetInventoryByProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        InventoryEntity inventory = getSampleInventory();
        when(inventoryService.getInventoryByProduct(eq(productId))).thenReturn(Collections.singletonList(inventory));
        mockMvc.perform(get("/api/inventory/product/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$0.quantity", is(100)));
    }

    @Test
    void testCreateInventory() throws Exception {
        InventoryEntity inventory = getSampleInventory();
        when(inventoryService.createInventory(any(InventoryEntity.class))).thenReturn(inventory);
        mockMvc.perform(post("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inventory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(100)));
    }

    @Test
    void testAdjustQuantity() throws Exception {
        AdjustmentRequest req = new AdjustmentRequest();
        req.setQuantity(10);
        req.setType(ChangeType.ADD);
        req.setStoreId(UUID.randomUUID());
        req.setUserId(UUID.randomUUID());
        req.setReason("Test adjustment");
        InventoryEntity inventory = getSampleInventory();
        when(inventoryService.adjustQuantity(anyString(), any(Integer.class), any(ChangeType.class), any(UUID.class), any(UUID.class), anyString())).thenReturn(inventory);
        mockMvc.perform(put("/api/inventory/adjust/testproduct")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(100)));
    }
} 