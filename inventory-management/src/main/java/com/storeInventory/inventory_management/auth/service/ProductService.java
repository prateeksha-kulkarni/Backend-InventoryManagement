package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.ProductResponseDto;
import com.storeInventory.inventory_management.auth.exception.ResourceNotFoundException;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    public ProductResponseDto getProductById(UUID productId) {
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        return ProductResponseDto.fromEntity(entity);
    }
    
    public Optional<ProductEntity> getProductBySku(String sku) {
        return productRepository.findBySku(sku);
    }
    
    public ProductResponseDto createProduct(ProductEntity product) {
        ProductEntity saved = productRepository.save(product);
        return ProductResponseDto.fromEntity(saved);
    }
    
    public ProductResponseDto updateProduct(UUID productId, ProductEntity productDetails) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        product.setName(productDetails.getName());
        product.setSku(productDetails.getSku());
        product.setCategory(productDetails.getCategory());
        product.setDescription(productDetails.getDescription());
        ProductEntity updated = productRepository.save(product);
        return ProductResponseDto.fromEntity(updated);
    }
    
    public void deleteProduct(UUID productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
    }
} 