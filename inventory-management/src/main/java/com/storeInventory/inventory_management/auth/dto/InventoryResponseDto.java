package com.storeInventory.inventory_management.auth.dto;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.Enum.ProductStatus;
import com.storeInventory.inventory_management.auth.model.InventoryEntity;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class InventoryResponseDto {
    private UUID inventoryId;
    private UUID productId;
    private String name;
    private ProductCategory category;
    private Integer quantity;
    private Integer minThreshold;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private LocalDateTime updatedAt;

    public static InventoryResponseDto fromEntity(InventoryEntity entity) {
        InventoryResponseDto dto = new InventoryResponseDto();
        dto.setInventoryId(entity.getInventoryId());
        dto.setProductId(entity.getProduct().getProductId());
        dto.setName(entity.getProduct().getName());
        dto.setCategory(entity.getProduct().getCategory());
        dto.setQuantity(entity.getQuantity());
        dto.setMinThreshold(entity.getMinThreshold());
        dto.setUpdatedAt(entity.getUpdatedAt());

        //  Add stock status logic
        int quantity = entity.getQuantity();
        int threshold = entity.getMinThreshold();
        if (quantity < threshold / 2) dto.setStatus(ProductStatus.LOW_STOCK);
        else if (quantity < threshold) dto.setStatus(ProductStatus.REORDER_SOON);
        else dto.setStatus(ProductStatus.IN_STOCK);

        return dto;
    }
}
