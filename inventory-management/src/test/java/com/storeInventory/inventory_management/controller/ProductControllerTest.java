package com.storeInventory.inventory_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeInventory.inventory_management.auth.controller.ProductController;
import com.storeInventory.inventory_management.auth.dto.ProductResponseDto;
import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = ProductController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfiguration.class)
)

public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductEntity getSampleProduct() {
        ProductEntity product = new ProductEntity();
        product.setProductId(UUID.randomUUID());
        product.setName("Test Product");
        product.setSku("SKU123");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setDescription("A test product");
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    private ProductResponseDto getSampleProductResponseDto(ProductEntity product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setCategory(product.getCategory());
        dto.setDescription(product.getDescription());
        dto.setCreatedAt(product.getCreatedAt());
        return dto;
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductEntity product = getSampleProduct();
        ProductResponseDto dto = getSampleProductResponseDto(product);
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Product")));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductEntity product = getSampleProduct();
        ProductResponseDto dto = getSampleProductResponseDto(product);
        when(productService.getProductById(eq(product.getProductId()))).thenReturn(dto);

        mockMvc.perform(get("/api/products/" + product.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is("SKU123")));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductEntity product = getSampleProduct();
        ProductResponseDto dto = getSampleProductResponseDto(product);
        when(productService.createProduct(any(ProductEntity.class))).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductEntity product = getSampleProduct();
        ProductResponseDto dto = getSampleProductResponseDto(product);
        when(productService.updateProduct(eq(product.getProductId()), any(ProductEntity.class))).thenReturn(dto);

        mockMvc.perform(put("/api/products/" + product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is("SKU123")));
    }

    @Test
    void testDeleteProductById() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/products/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteProductByEntity() throws Exception {
        ProductEntity product = getSampleProduct();
        mockMvc.perform(post("/api/products/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isNoContent());
    }

    @Test
    void testSearchProducts() throws Exception {
        ProductEntity product = getSampleProduct();
        ProductResponseDto dto = getSampleProductResponseDto(product);
        when(productService.searchProducts(anyString())).thenReturn(Collections.singletonList(dto));
        mockMvc.perform(get("/api/products/search?query=test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku", is("SKU123")));
    }

    @Test
    void testGetProductBySku_found() throws Exception {
        ProductEntity product = getSampleProduct();
        when(productRepository.findBySku(eq(product.getSku()))).thenReturn(Optional.of(product));
        mockMvc.perform(get("/api/products/sku/" + product.getSku()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Product")));
    }

    @Test
    void testGetProductBySku_notFound() throws Exception {
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/products/sku/DOES_NOT_EXIST"))
                .andExpect(status().isNotFound());
    }
} 