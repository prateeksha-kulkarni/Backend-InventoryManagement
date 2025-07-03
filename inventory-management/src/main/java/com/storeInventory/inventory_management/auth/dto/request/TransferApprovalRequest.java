package com.storeInventory.inventory_management.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferApprovalRequest {
    
    @NotNull(message = "Transfer ID is required")
    private UUID transferId;
    
    @NotNull(message = "Approval status is required")
    private Boolean approved;
    
    private String reason;
} 