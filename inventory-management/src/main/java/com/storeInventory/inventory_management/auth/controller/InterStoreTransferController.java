package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.TransferResponseDto;

import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.service.InterStoreTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import java.util.UUID;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class InterStoreTransferController {
    private final InterStoreTransferService transferService;
    
    @GetMapping("/logs")
public ResponseEntity<List<TransferResponseDto>> getAllTransfers() {
    List<InterStoreTransferEntity> transfers = transferService.getAllTransfers();
    List<TransferResponseDto> dtos = transfers.stream()
            .map(TransferResponseDto::fromEntity)
            .toList();

    return ResponseEntity.ok(dtos);
}
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
    @GetMapping("/history/{storeId}")
    public ResponseEntity<List<TransferResponseDto>> getTransferHistory(@PathVariable UUID storeId) {
        List<InterStoreTransferEntity> transfers = transferService.getTransferHistoryForStore(storeId);
        List<TransferResponseDto> dtos = transfers.stream()
                .map(TransferResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);


    }
    @PutMapping("/{transferId}/reject")
    public ResponseEntity<String> rejectTransfer(@PathVariable UUID transferId) {
        try {
            transferService.rejectTransfer(transferId);
            return ResponseEntity.ok("Transfer rejected successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/to/{storeId}/dto")
    public ResponseEntity<List<TransferResponseDto>> getPendingTransfers(
            @PathVariable UUID storeId,
            @RequestParam TransferStatus status) {
        List<InterStoreTransferEntity> transfers = transferService.getTransfersToStoreByStatus(storeId, status);
        List<TransferResponseDto> dtos = transfers.stream()
                .map(TransferResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }


} 