package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.InventoryReportController;
import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.service.InventoryReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = InventoryReportController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class InventoryReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryReportService inventoryReportService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryTurnoverRequest getSampleRequest() {
        InventoryTurnoverRequest request = new InventoryTurnoverRequest();
        request.setStartDate(LocalDate.now().minusDays(30));
        request.setEndDate(LocalDate.now());
        return request;
    }

    private InventoryTurnoverResponse getSampleResponse() {
        InventoryTurnoverResponse response = new InventoryTurnoverResponse();
        response.setProductName("Test Product");
        response.setStoreName("Test Store");
        response.setQuantity(100);
        response.setTurnoverRate(0.5);
        return response;
    }

    @Test
    void testGetTurnoverReport() throws Exception {
        InventoryTurnoverRequest request = getSampleRequest();
        InventoryTurnoverResponse response = getSampleResponse();
        when(inventoryReportService.calculateTurnover(any(InventoryTurnoverRequest.class))).thenReturn(Collections.singletonList(response));
        mockMvc.perform(post("/inventory-analytics/turnover")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName", is("Test Product")))
                .andExpect(jsonPath("$[0].storeName", is("Test Store")));
    }
} 