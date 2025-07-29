package com.storeInventory.inventory_management.auth.service;

import com.storeInventory.inventory_management.auth.dto.ProductResponseDto;
import com.storeInventory.inventory_management.auth.exception.ResourceNotFoundException;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productService = new ProductService(productRepository);
    }

    @Test
    void getAllProductsReturnsEmptyListWhenNoProducts() {
        // Given
        doReturn(Collections.emptyList()).when(productRepository).findAll();
        // When
        List<ProductResponseDto> result = productService.getAllProducts();
        // Then
        assertThat(result, is(empty()));
        verify(productRepository, atLeast(1)).findAll();
    }

    @Test
    void getAllProductsReturnsSingleProduct() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity productEntity = createProductEntity(productId, "Test Product", "TEST-001", ProductCategory.ELECTRONICS, "Test Description");
        doReturn(Arrays.asList(productEntity)).when(productRepository).findAll();
        // When
        List<ProductResponseDto> result = productService.getAllProducts();
        // Then
        assertThat(result, hasSize(1));
        ProductResponseDto dto = result.get(0);
        assertEquals(productId, dto.getProductId());
        assertEquals("Test Product", dto.getName());
        assertEquals("TEST-001", dto.getSku());
        assertEquals(ProductCategory.ELECTRONICS, dto.getCategory());
        assertEquals("Test Description", dto.getDescription());
        verify(productRepository, atLeast(1)).findAll();
    }

    @Test
    void getAllProductsReturnsMultipleProducts() {
        // Given
        UUID productId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID productId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        ProductEntity product1 = createProductEntity(productId1, "Product 1", "TEST-001", ProductCategory.ELECTRONICS, "Description 1");
        ProductEntity product2 = createProductEntity(productId2, "Product 2", "TEST-002", ProductCategory.CLOTHING, "Description 2");
        doReturn(Arrays.asList(product1, product2)).when(productRepository).findAll();
        // When
        List<ProductResponseDto> result = productService.getAllProducts();
        // Then
        assertThat(result, hasSize(2));
        verify(productRepository, atLeast(1)).findAll();
    }

    @Test
    void getProductByIdReturnsProductWhenExists() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity productEntity = createProductEntity(productId, "Test Product", "TEST-001", ProductCategory.ELECTRONICS, "Test Description");
        doReturn(Optional.of(productEntity)).when(productRepository).findById(productId);
        // When
        ProductResponseDto result = productService.getProductById(productId);
        // Then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Test Product", result.getName());
        assertEquals("TEST-001", result.getSku());
        assertEquals(ProductCategory.ELECTRONICS, result.getCategory());
        assertEquals("Test Description", result.getDescription());
        verify(productRepository, atLeast(1)).findById(productId);
    }

    @Test
    void getProductByIdThrowsExceptionWhenNotFound() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        doReturn(Optional.empty()).when(productRepository).findById(productId);
        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
        assertThat(exception.getMessage(), equalTo("Product not found with productId : '" + productId + "'"));
        verify(productRepository, atLeast(1)).findById(productId);
    }

    @Test
    void getProductBySkuReturnsOptionalWithProductWhenExists() {
        // Given
        String sku = "TEST-001";
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity productEntity = createProductEntity(productId, "Test Product", sku, ProductCategory.ELECTRONICS, "Test Description");
        doReturn(Optional.of(productEntity)).when(productRepository).findBySku(sku);
        // When
        Optional<ProductEntity> result = productService.getProductBySku(sku);
        // Then
        assertTrue(result.isPresent());
        assertEquals(productId, result.get().getProductId());
        assertEquals("Test Product", result.get().getName());
        assertEquals(sku, result.get().getSku());
        verify(productRepository, atLeast(1)).findBySku(sku);
    }

    @Test
    void getProductBySkuReturnsEmptyOptionalWhenNotFound() {
        // Given
        String sku = "NON-EXISTENT";
        doReturn(Optional.empty()).when(productRepository).findBySku(sku);
        // When
        Optional<ProductEntity> result = productService.getProductBySku(sku);
        // Then
        assertTrue(result.isEmpty());
        verify(productRepository, atLeast(1)).findBySku(sku);
    }

    @Test
    void createProductReturnsSavedProduct() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity inputProduct = createProductEntity(null, "New Product", "NEW-001", ProductCategory.FOOD, "New Description");
        ProductEntity savedProduct = createProductEntity(productId, "New Product", "NEW-001", ProductCategory.FOOD, "New Description");
        doReturn(savedProduct).when(productRepository).save(inputProduct);
        // When
        ProductResponseDto result = productService.createProduct(inputProduct);
        // Then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("New Product", result.getName());
        assertEquals("NEW-001", result.getSku());
        assertEquals(ProductCategory.FOOD, result.getCategory());
        assertEquals("New Description", result.getDescription());
        verify(productRepository, atLeast(1)).save(inputProduct);
    }

    @Test
    void updateProductUpdatesExistingProduct() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity existingProduct = createProductEntity(productId, "Old Product", "OLD-001", ProductCategory.ELECTRONICS, "Old Description");
        ProductEntity updateDetails = createProductEntity(null, "Updated Product", "UPD-001", ProductCategory.CLOTHING, "Updated Description");
        ProductEntity updatedProduct = createProductEntity(productId, "Updated Product", "UPD-001", ProductCategory.CLOTHING, "Updated Description");
        doReturn(Optional.of(existingProduct)).when(productRepository).findById(productId);
        doReturn(updatedProduct).when(productRepository).save(existingProduct);
        // When
        ProductResponseDto result = productService.updateProduct(productId, updateDetails);
        // Then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Updated Product", result.getName());
        assertEquals("UPD-001", result.getSku());
        assertEquals(ProductCategory.CLOTHING, result.getCategory());
        assertEquals("Updated Description", result.getDescription());
        verify(productRepository, atLeast(1)).findById(productId);
        verify(productRepository, atLeast(1)).save(existingProduct);
    }

    @Test
    void updateProductThrowsExceptionWhenNotFound() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity updateDetails = createProductEntity(null, "Updated Product", "UPD-001", ProductCategory.CLOTHING, "Updated Description");
        doReturn(Optional.empty()).when(productRepository).findById(productId);
        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, updateDetails));
        assertThat(exception.getMessage(), equalTo("Product not found with productId : '" + productId + "'"));
        verify(productRepository, atLeast(1)).findById(productId);
    }

    @Test
    void deleteProductDeletesExistingProduct() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity existingProduct = createProductEntity(productId, "Product to Delete", "DEL-001", ProductCategory.HOME_GOODS, "Delete Description");
        doReturn(Optional.of(existingProduct)).when(productRepository).findById(productId);
        // When
        productService.deleteProduct(productId);
        // Then
        verify(productRepository, atLeast(1)).findById(productId);
        verify(productRepository, atLeast(1)).delete(existingProduct);
    }

    @Test
    void deleteProductThrowsExceptionWhenNotFound() {
        // Given
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        doReturn(Optional.empty()).when(productRepository).findById(productId);
        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId));
        assertThat(exception.getMessage(), equalTo("Product not found with productId : '" + productId + "'"));
        verify(productRepository, atLeast(1)).findById(productId);
    }

    @Test
    void searchProductsReturnsEmptyListWhenNoMatches() {
        // Given
        String query = "nonexistent";
        doReturn(Collections.emptyList()).when(productRepository).searchProducts(query);
        // When
        List<ProductResponseDto> result = productService.searchProducts(query);
        // Then
        assertThat(result, is(empty()));
        verify(productRepository, atLeast(1)).searchProducts(query);
    }

    @Test
    void searchProductsReturnsMatchingProducts() {
        // Given
        String query = "electronics";
        UUID productId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        UUID productId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        ProductEntity product1 = createProductEntity(productId1, "Electronics Device", "ELEC-001", ProductCategory.ELECTRONICS, "Electronic Description");
        ProductEntity product2 = createProductEntity(productId2, "Another Electronics", "ELEC-002", ProductCategory.ELECTRONICS, "Another Electronic");
        doReturn(Arrays.asList(product1, product2)).when(productRepository).searchProducts(query);
        // When
        List<ProductResponseDto> result = productService.searchProducts(query);
        // Then
        assertThat(result, hasSize(2));
        verify(productRepository, atLeast(1)).searchProducts(query);
    }

    @Test
    void searchProductsReturnsSingleMatchingProduct() {
        // Given
        String query = "unique";
        UUID productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ProductEntity product = createProductEntity(productId, "Unique Product", "UNQ-001", ProductCategory.OFFICE_SUPPLIES, "Unique Description");
        doReturn(Arrays.asList(product)).when(productRepository).searchProducts(query);
        // When
        List<ProductResponseDto> result = productService.searchProducts(query);
        // Then
        assertThat(result, hasSize(1));
        ProductResponseDto dto = result.get(0);
        assertEquals(productId, dto.getProductId());
        assertEquals("Unique Product", dto.getName());
        assertEquals("UNQ-001", dto.getSku());
        assertEquals(ProductCategory.OFFICE_SUPPLIES, dto.getCategory());
        assertEquals("Unique Description", dto.getDescription());
        verify(productRepository, atLeast(1)).searchProducts(query);
    }

    @Test
    void searchProductsWithNullQuery() {
        // Given
        String query = null;
        doReturn(Collections.emptyList()).when(productRepository).searchProducts(query);
        // When
        List<ProductResponseDto> result = productService.searchProducts(query);
        // Then
        assertThat(result, is(empty()));
        verify(productRepository, atLeast(1)).searchProducts(query);
    }

    @Test
    void searchProductsWithEmptyQuery() {
        // Given
        String query = "";
        doReturn(Collections.emptyList()).when(productRepository).searchProducts(query);
        // When
        List<ProductResponseDto> result = productService.searchProducts(query);
        // Then
        assertThat(result, is(empty()));
        verify(productRepository, atLeast(1)).searchProducts(query);
    }

    private ProductEntity createProductEntity(UUID productId, String name, String sku, ProductCategory category, String description) {
        ProductEntity entity = new ProductEntity();
        entity.setProductId(productId);
        entity.setName(name);
        entity.setSku(sku);
        entity.setCategory(category);
        entity.setDescription(description);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }
}
