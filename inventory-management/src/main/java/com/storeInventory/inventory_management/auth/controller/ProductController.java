package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.ProductResponseDto;
import com.storeInventory.inventory_management.auth.dto.ProductSearchDto;

import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }



    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductEntity product) {
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID id, @RequestBody ProductEntity product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteProduct(@RequestBody ProductEntity product) {
        productService.deleteProduct(product.getProductId());
        return ResponseEntity.noContent().build(); // HTTP 204
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ProductSearchDto>> searchProducts(@RequestParam("query") String query) {
//        List<ProductSearchDto> result = productService.searchProductsByName(query);
//        return ResponseEntity.ok(result);
//    }
     @GetMapping("/search")
     public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam String query) {
                return ResponseEntity.ok(productService.searchProducts(query));
     }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductEntity> getProductBySku(@PathVariable String sku) {
        return ResponseEntity.ok(
                productRepository.findBySku(sku)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SKU not found"))
        );
    }
}

