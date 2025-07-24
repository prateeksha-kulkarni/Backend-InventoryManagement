package com.storeInventory.inventory_management.controller;

import com.storeInventory.inventory_management.auth.controller.ProductController;
import com.storeInventory.inventory_management.auth.dto.ProductResponseDto;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Timeout(10)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts() {
        ProductResponseDto productDto1 = mock(ProductResponseDto.class);
        ProductResponseDto productDto2 = mock(ProductResponseDto.class);
        List<ProductResponseDto> expectedProducts = Arrays.asList(productDto1, productDto2);
        doReturn(expectedProducts).when(productService).getAllProducts();
        ResponseEntity<List<ProductResponseDto>> response = productController.getAllProducts();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedProducts)));
        verify(productService, atLeast(1)).getAllProducts();
    }

    @Test
    public void testGetProductById() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductResponseDto expectedProduct = mock(ProductResponseDto.class);
        doReturn(expectedProduct).when(productService).getProductById(productId);
        ResponseEntity<ProductResponseDto> response = productController.getProductById(productId);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedProduct)));
        verify(productService, atLeast(1)).getProductById(productId);
    }

    @Test
    public void testCreateProduct() {
        ProductEntity productEntity = mock(ProductEntity.class);
        ProductResponseDto expectedProduct = mock(ProductResponseDto.class);
        doReturn(expectedProduct).when(productService).createProduct(productEntity);
        ResponseEntity<ProductResponseDto> response = productController.createProduct(productEntity);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedProduct)));
        verify(productService, atLeast(1)).createProduct(productEntity);
    }

    @Test
    public void testUpdateProduct() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductEntity productEntity = mock(ProductEntity.class);
        ProductResponseDto expectedProduct = mock(ProductResponseDto.class);
        doReturn(expectedProduct).when(productService).updateProduct(productId, productEntity);
        ResponseEntity<ProductResponseDto> response = productController.updateProduct(productId, productEntity);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedProduct)));
        verify(productService, atLeast(1)).updateProduct(productId, productEntity);
    }

    @Test
    public void testDeleteProduct() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        doNothing().when(productService).deleteProduct(productId);
        ResponseEntity<Void> response = productController.deleteProduct(productId);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(productId);
    }

    @Test
    public void testDeleteProductByEntity() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductEntity productEntity = mock(ProductEntity.class);
        when(productEntity.getProductId()).thenReturn(productId);
        doNothing().when(productService).deleteProduct(productId);
        ResponseEntity<Void> response = productController.deleteProduct(productEntity);
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService, times(1)).deleteProduct(productId);
        verify(productEntity, times(1)).getProductId();
    }

    @Test
    public void testSearchProducts() {
        String query = "test query";
        ProductResponseDto productDto1 = mock(ProductResponseDto.class);
        ProductResponseDto productDto2 = mock(ProductResponseDto.class);
        List<ProductResponseDto> expectedProducts = Arrays.asList(productDto1, productDto2);
        doReturn(expectedProducts).when(productService).searchProducts(query);
        ResponseEntity<List<ProductResponseDto>> response = productController.searchProducts(query);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedProducts)));
        verify(productService, atLeast(1)).searchProducts(query);
    }

    @Test
    public void testGetProductBySkuSuccess() {
        String sku = "TEST-SKU-001";
        ProductEntity expectedProduct = mock(ProductEntity.class);
        doReturn(Optional.of(expectedProduct)).when(productRepository).findBySku(sku);
        ResponseEntity<ProductEntity> response = productController.getProductBySku(sku);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(equalTo(expectedProduct)));
        verify(productRepository, atLeast(1)).findBySku(sku);
    }

    @Test
    public void testGetProductBySkuNotFound() {
        String sku = "NON-EXISTENT-SKU";
        doReturn(Optional.empty()).when(productRepository).findBySku(sku);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> productController.getProductBySku(sku));
        assertThat(exception.getStatusCode(), is(equalTo(HttpStatus.NOT_FOUND)));
        assertThat(exception.getReason(), is(equalTo("SKU not found")));
        verify(productRepository, atLeast(1)).findBySku(sku);
    }

    @Test
    public void testGetAllProductsEmptyList() {
        List<ProductResponseDto> emptyList = Arrays.asList();
        doReturn(emptyList).when(productService).getAllProducts();
        ResponseEntity<List<ProductResponseDto>> response = productController.getAllProducts();
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().size(), is(equalTo(0)));
        verify(productService, atLeast(1)).getAllProducts();
    }

    @Test
    public void testSearchProductsEmptyQuery() {
        String query = "";
        List<ProductResponseDto> expectedProducts = Arrays.asList();
        doReturn(expectedProducts).when(productService).searchProducts(query);
        ResponseEntity<List<ProductResponseDto>> response = productController.searchProducts(query);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody().size(), is(equalTo(0)));
        verify(productService, atLeast(1)).searchProducts(query);
    }
}
