package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.request.InventoryTurnoverRequest;
import com.storeInventory.inventory_management.auth.dto.response.InventoryTurnoverResponse;
import com.storeInventory.inventory_management.auth.model.Enum.TransferStatus;
import com.storeInventory.inventory_management.auth.model.InterStoreTransferEntity;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.repository.InterStoreTransferRepository;
import com.storeInventory.inventory_management.auth.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class InventoryReportService {

    private final InventoryRepository inventoryRepository;
    private final InterStoreTransferRepository transferRepository;

    public InventoryReportService(InventoryRepository inventoryRepository,
                                  InterStoreTransferRepository transferRepository) {
        this.inventoryRepository = inventoryRepository;
        this.transferRepository = transferRepository;
    }

    public List<InventoryTurnoverResponse> calculateTurnover(InventoryTurnoverRequest request) {
        LocalDateTime start = request.getStartDate().atStartOfDay();
        LocalDateTime end = request.getEndDate().atTime(LocalTime.MAX);

        List<InterStoreTransferEntity> transfers = transferRepository
                .findByStatusAndTimestampBetween(TransferStatus.APPROVED, start, end);


        Map<String, Integer> outflowMap = new HashMap<>();
        for (InterStoreTransferEntity transfer : transfers) {
            UUID storeId = transfer.getFromStore().getStoreId();
            UUID productId = transfer.getProduct().getProductId();
            int quantity = transfer.getQuantity();

            String key = storeId + "-" + productId;
            outflowMap.put(key, outflowMap.getOrDefault(key, 0) + quantity);
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


            InventoryTurnoverResponse dto = new InventoryTurnoverResponse();
            dto.setStoreId(storeId);
            dto.setStoreName(storeName);
            dto.setProductId(productId);
            dto.setProductName(productName);
            dto.setQuantity(quantity);
            dto.setMinThreshold(minThreshold);
            dto.setStockStatus(stockStatus);
            dto.setTurnoverRate(turnoverRate);

            responseList.add(dto);
        }

        return responseList;
    }
}