package com.storeInventory.inventory_management.auth.dto;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductResponseDto {
    private UUID productId;
    private String name;
    private String sku;
    private ProductCategory category;
    private String description;
    private LocalDateTime createdAt;



    public static ProductResponseDto fromEntity(ProductEntity entity) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setProductId(entity.getProductId());
        dto.setName(entity.getName());
        dto.setSku(entity.getSku());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
} 