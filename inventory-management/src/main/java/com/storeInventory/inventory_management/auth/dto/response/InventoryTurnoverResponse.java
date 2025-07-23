package com.storeInventory.inventory_management.auth.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryTurnoverResponse {
    private UUID storeId;
    private String storeName;
    private UUID productId;
    private String productName;
    private int quantity;
    private int minThreshold;
    private String stockStatus;
    private double turnoverRate;
    private double salesTurnoverRate;
}