package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.request.AdjustmentRequest;
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
    public ResponseEntity<List<InventoryResponseDto>> getAllInventory() {
        List<InventoryEntity> inventoryEntities = inventoryService.getAllInventory();
        List<InventoryResponseDto> dtos = inventoryEntities.stream()
                .map(InventoryResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<InventoryResponseDto>> searchInventory(@RequestParam String query,
                                                                      @RequestParam(required = false) UUID storeId,
                                                                      @RequestParam(required = false) String fields) {
        return ResponseEntity.ok(inventoryService.searchInventory(query, storeId));
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

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryResponseDto>> getInventoryByProduct(@PathVariable UUID productId) {
        List<InventoryEntity> inventories = inventoryService.getInventoryByProduct(productId);
        List<InventoryResponseDto> dtos = inventories.stream()
                .map(InventoryResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }



    @PostMapping
    public ResponseEntity<InventoryEntity> createInventory(@RequestBody InventoryEntity inventory) {
        return ResponseEntity.ok(inventoryService.createInventory(inventory));
    }

    @PutMapping("/adjust/{productName}")
    public ResponseEntity<InventoryResponseDto> adjustQuantity(
            @RequestBody AdjustmentRequest request,
            @PathVariable String productName) {

        InventoryEntity updated = inventoryService.adjustQuantity(
                productName,
                request.getQuantity(),
                request.getType(),
                request.getStoreId(),
                request.getUserId(),
                request.getReason()
        );

        return ResponseEntity.ok(InventoryResponseDto.fromEntity(updated));
    }
} 