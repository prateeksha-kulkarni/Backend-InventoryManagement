package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.service.StockAdjustmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.storeInventory.inventory_management.auth.dto.response.StockAdjustmentResponseDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock-adjustments")
@RequiredArgsConstructor
public class StockAdjustmentController {
    private final StockAdjustmentService stockAdjustmentService;

    
    @GetMapping
public ResponseEntity<List<StockAdjustmentResponseDto>> getAllAdjustments() {
    List<StockAdjustmentResponseDto> dtos = stockAdjustmentService.getAllAdjustments().stream()
        .map(StockAdjustmentResponseDto::fromEntity)
        .toList();
    return ResponseEntity.ok(dtos);
}

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<List<StockAdjustmentEntity>> getAdjustmentsByInventory(@PathVariable UUID inventoryId) {
        return ResponseEntity.ok(stockAdjustmentService.getAdjustmentsByInventory(inventoryId));
    }

    @PostMapping
    public ResponseEntity<StockAdjustmentEntity> createAdjustment(@RequestBody StockAdjustmentEntity adjustment) {
        return ResponseEntity.ok(stockAdjustmentService.createAdjustment(adjustment));
    }
} 