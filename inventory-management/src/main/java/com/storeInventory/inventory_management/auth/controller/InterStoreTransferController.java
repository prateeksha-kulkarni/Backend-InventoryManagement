package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.service.InterStoreTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class InterStoreTransferController {
    private final InterStoreTransferService transferService;

    @GetMapping("/to/{storeId}")
    public ResponseEntity<List<InterStoreTransferEntity>> getTransfersToStore(@PathVariable UUID storeId, @RequestParam TransferStatus status) {
        return ResponseEntity.ok(transferService.getTransfersToStore(storeId, status));
    }

    @GetMapping("/from/{storeId}")
    public ResponseEntity<List<InterStoreTransferEntity>> getTransfersFromStore(@PathVariable UUID storeId) {
        return ResponseEntity.ok(transferService.getTransfersFromStore(storeId));
    }

    @PostMapping
    public ResponseEntity<InterStoreTransferEntity> createTransfer(@RequestBody InterStoreTransferEntity transfer) {
        return ResponseEntity.ok(transferService.createTransfer(transfer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InterStoreTransferEntity> updateTransfer(@PathVariable UUID id, @RequestBody InterStoreTransferEntity transfer) {
        transfer.setTransferId(id);
        return ResponseEntity.ok(transferService.updateTransfer(transfer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterStoreTransferEntity> getTransferById(@PathVariable UUID id) {
        return ResponseEntity.ok(transferService.getTransferById(id));
    }
} 