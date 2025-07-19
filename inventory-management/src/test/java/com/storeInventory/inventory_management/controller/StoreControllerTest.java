package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.StoreController;
import com.storeInventory.inventory_management.auth.model.StoreEntity;
import com.storeInventory.inventory_management.auth.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = StoreController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)
public class StoreControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreEntity getSampleStore() {
        StoreEntity store = new StoreEntity();
        store.setStoreId(UUID.randomUUID());
        store.setName("Test Store");
        store.setLocation("Test Location");
        return store;
    }

    @Test
    void testGetAllStores() throws Exception {
        StoreEntity store = getSampleStore();
        when(storeService.getAllStores()).thenReturn(Collections.singletonList(store));
        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Store")))
                .andExpect(jsonPath("$[0].location", is("Test Location")));
    }

    @Test
    void testGetStoreById() throws Exception {
        StoreEntity store = getSampleStore();
        when(storeService.getStoreById(eq(store.getStoreId()))).thenReturn(store);
        mockMvc.perform(get("/api/stores/" + store.getStoreId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Store")));
    }

    @Test
    void testCreateStore() throws Exception {
        StoreEntity store = getSampleStore();
        when(storeService.createStore(any(StoreEntity.class))).thenReturn(store);
        mockMvc.perform(post("/api/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(store)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Store")));
    }
} 