package com.storeInventory.inventory_management.auth.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    
    @NotNull(message = "Product ID is required and must be a valid UUID (format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)")
    private UUID productId;
    
    @NotNull(message = "From store ID is required and must be a valid UUID (format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)")
    private UUID fromStoreId;
    
    @NotNull(message = "To store ID is required and must be a valid UUID (format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)")
    private UUID toStoreId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Requested by ID is required and must be a valid UUID (format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)")
    private UUID requestedById;
} 