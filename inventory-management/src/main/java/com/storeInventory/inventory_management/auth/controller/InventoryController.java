package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.service.InventoryService;
import com.storeInventory.inventory_management.auth.dto.InventoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<InventoryEntity>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<InventoryResponseDto>> getInventoryByStore(@PathVariable UUID storeId) {
        List<InventoryEntity> inventoryEntities = inventoryService.getInventoryByStore(storeId);
        List<InventoryResponseDto> dtos = inventoryEntities.stream()
            .map(InventoryResponseDto::fromEntity)
            .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/store/{storeId}/product/{productId}")
    public ResponseEntity<Optional<InventoryEntity>> getInventoryByStoreAndProduct(@PathVariable UUID storeId, @PathVariable UUID productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByStoreAndProduct(storeId, productId));
    }

    @PostMapping
    public ResponseEntity<InventoryEntity> createInventory(@RequestBody InventoryEntity inventory) {
        return ResponseEntity.ok(inventoryService.createInventory(inventory));
    }
} 