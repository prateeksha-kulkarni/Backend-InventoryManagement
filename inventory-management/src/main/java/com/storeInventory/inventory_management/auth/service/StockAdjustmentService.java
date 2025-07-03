package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockAdjustmentService {
    private final StockAdjustmentRepository stockAdjustmentRepository;

    public List<StockAdjustmentEntity> getAllAdjustments() {
        return stockAdjustmentRepository.findAll();
    }

    public List<StockAdjustmentEntity> getAdjustmentsByInventory(UUID inventoryId) {
        return stockAdjustmentRepository.findAll().stream()
                .filter(adj -> adj.getInventory().getInventoryId().equals(inventoryId))
                .toList();
    }

    public StockAdjustmentEntity createAdjustment(StockAdjustmentEntity adjustment) {
        return stockAdjustmentRepository.save(adjustment);
    }
} 