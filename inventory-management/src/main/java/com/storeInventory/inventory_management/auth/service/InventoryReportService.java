package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.StockAdjustmentEntity;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import com.storeInventory.inventory_management.auth.repository.StockAdjustmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class InventoryReportService {

    private final InventoryRepository inventoryRepository;
    private final InterStoreTransferRepository transferRepository;
    private final StockAdjustmentRepository stockAdjustmentRepository;

    public InventoryReportService(InventoryRepository inventoryRepository,
                                  InterStoreTransferRepository transferRepository,
                                  StockAdjustmentRepository stockAdjustmentRepository) {
        this.inventoryRepository = inventoryRepository;
        this.transferRepository = transferRepository;
        this.stockAdjustmentRepository = stockAdjustmentRepository;
    }

    public List<InventoryTurnoverResponse> calculateTurnover(InventoryTurnoverRequest request) {
        LocalDateTime start = request.getStartDate().atStartOfDay();
        LocalDateTime end = request.getEndDate().atTime(LocalTime.MAX);

        // Get Inter store Transfers
        List<InterStoreTransferEntity> transfers = transferRepository
                .findByStatusAndTimestampBetween(TransferStatus.COMPLETED, start, end);

        Map<String, Integer> outflowMap = new HashMap<>();
        for (InterStoreTransferEntity transfer : transfers) {
            UUID storeId = transfer.getFromStore().getStoreId();
            UUID productId = transfer.getProduct().getProductId();
            int quantity = transfer.getQuantity();

            String key = storeId + "-" + productId;
            outflowMap.put(key, outflowMap.getOrDefault(key, 0) + quantity);
        }

        // Get Stock Adjustments of type REMOVE (sales)
        List<StockAdjustmentEntity> salesAdjustments = stockAdjustmentRepository.findAll().stream()
                .filter(adj -> adj.getChangeType() == ChangeType.REMOVE)
                .filter(adj -> !adj.getTimestamp().isBefore(start) && !adj.getTimestamp().isAfter(end))
                .toList();

        Map<String, Integer> salesMap = new HashMap<>();
        for (StockAdjustmentEntity sale : salesAdjustments) {
            UUID storeId = sale.getInventory().getStore().getStoreId();
            UUID productId = sale.getInventory().getProduct().getProductId();
            int quantity = sale.getQuantityChange();

            String key = storeId + "-" + productId;
            salesMap.put(key, salesMap.getOrDefault(key, 0) + quantity);
        }

        List<InventoryEntity> inventoryList = inventoryRepository.findAll();
        List<InventoryTurnoverResponse> responseList = new ArrayList<>();

        for (InventoryEntity inventory : inventoryList) {
            UUID storeId = inventory.getStore().getStoreId();
            String storeName = inventory.getStore().getName();
            UUID productId = inventory.getProduct().getProductId();
            String productName = inventory.getProduct().getName();
            int quantity = inventory.getQuantity();
            int minThreshold = inventory.getMinThreshold();

            String stockStatus;
            if (quantity <= minThreshold) {
                stockStatus = "Stockout";
            } else if (quantity <= 2 * minThreshold) {
                stockStatus = "Healthy";
            } else {
                stockStatus = "Overstock";
            }

            String key = storeId + "-" + productId;

            int outflow = outflowMap.getOrDefault(key, 0);
            double turnoverRate = quantity == 0 ? 0.0 : (double) outflow / quantity;

            int sales = salesMap.getOrDefault(key, 0);
            double salesTurnoverRate = quantity == 0 ? 0.0 : (double) sales / quantity;

            InventoryTurnoverResponse dto = new InventoryTurnoverResponse();
            dto.setStoreId(storeId);
            dto.setStoreName(storeName);
            dto.setProductId(productId);
            dto.setProductName(productName);
            dto.setQuantity(quantity);
            dto.setMinThreshold(minThreshold);
            dto.setStockStatus(stockStatus);
            dto.setTurnoverRate(turnoverRate);
            dto.setSalesTurnoverRate(salesTurnoverRate);

            responseList.add(dto);
        }

        return responseList;
    }
}
