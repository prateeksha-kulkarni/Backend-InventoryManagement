package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.StockAdjustmentController;
import com.storeInventory.inventory_management.auth.dto.response.StockAdjustmentResponseDto;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.service.StockAdjustmentService;
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
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = StockAdjustmentController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class StockAdjustmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockAdjustmentService stockAdjustmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private StockAdjustmentEntity getSampleAdjustment() {
        StockAdjustmentEntity adj = new StockAdjustmentEntity();
        adj.setAdjustmentId(UUID.randomUUID());
        adj.setReason("Test Reason");
        adj.setQuantityChange(10);
        return adj;
    }

    private StockAdjustmentResponseDto getSampleResponseDto(StockAdjustmentEntity adj) {
        StockAdjustmentResponseDto dto = new StockAdjustmentResponseDto();
        dto.setAdjustmentId(adj.getAdjustmentId());
        dto.setReason(adj.getReason());
        dto.setQuantityChange(adj.getQuantityChange());
        return dto;
    }

    @Test
    void testGetAllAdjustments() throws Exception {
        StockAdjustmentEntity adj = getSampleAdjustment();
        when(stockAdjustmentService.getAllAdjustments()).thenReturn(Collections.singletonList(adj));
        mockMvc.perform(get("/api/stock-adjustments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reason", is("Test Reason")));
    }

    @Test
    void testGetAdjustmentsByInventory() throws Exception {
        UUID inventoryId = UUID.randomUUID();
        StockAdjustmentEntity adj = getSampleAdjustment();
        when(stockAdjustmentService.getAdjustmentsByInventory(eq(inventoryId))).thenReturn(Collections.singletonList(adj));
        mockMvc.perform(get("/api/stock-adjustments/inventory/" + inventoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reason", is("Test Reason")));
    }

    @Test
    void testCreateAdjustment() throws Exception {
        StockAdjustmentEntity adj = getSampleAdjustment();
        when(stockAdjustmentService.createAdjustment(any(StockAdjustmentEntity.class))).thenReturn(adj);
        mockMvc.perform(post("/api/stock-adjustments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adj)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reason", is("Test Reason")));
    }
} 