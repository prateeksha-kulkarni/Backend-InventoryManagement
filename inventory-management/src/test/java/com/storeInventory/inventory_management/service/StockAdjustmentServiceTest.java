package com.storeInventory.inventory_management.service;

import com.storeInventory.inventory_management.auth.service.StockAdjustmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class StockAdjustmentServiceTest {
    @InjectMocks
    private StockAdjustmentService stockAdjustmentService;

    @Test
    void contextLoads() {
        assertNotNull(stockAdjustmentService);
    }
} 