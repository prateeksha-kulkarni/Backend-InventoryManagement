package com.storeInventory.inventory_management.auth.dto;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchDto {

        private UUID productId;
        private String name;
        private String sku;
    }


