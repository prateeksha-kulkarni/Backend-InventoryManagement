package com.storeInventory.inventory_management.auth.dto.request;

import com.storeInventory.inventory_management.auth.model.Enum.ChangeType;
import lombok.Data;

import java.util.UUID;

@Data
public class AdjustmentRequest {
    private int quantity;
    private ChangeType type;
    private UUID storeId;
    private UUID userId;
    private String reason;
}
