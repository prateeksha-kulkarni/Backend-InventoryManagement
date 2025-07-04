package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.service.InventoryReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/inventory-analytics")
public class InventoryReportController {
    private final InventoryReportService inventoryReportService;

    public InventoryReportController(InventoryReportService inventoryReportService) {
        this.inventoryReportService = inventoryReportService;
    }

    @PostMapping("/turnover")
    public ResponseEntity<List<InventoryTurnoverResponse>> getTurnoverReport(
            @RequestBody InventoryTurnoverRequest request) {
        List<InventoryTurnoverResponse> report = inventoryReportService.calculateTurnover(request);
        return ResponseEntity.ok(report);
    }
}
