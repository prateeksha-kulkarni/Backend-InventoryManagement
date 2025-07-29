package com.storeInventory.inventory_management.auth.controller;

import com.storeInventory.inventory_management.auth.dto.ProductResponseDto;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import com.storeInventory.inventory_management.auth.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atLeast;

@Timeout(10)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productController = new ProductController(productService);
        productController.productRepository = productRepository;
    }

    @Test
    public void testGetAllProducts() {
        List<ProductResponseDto> expectedProducts = new ArrayList<>();
        ProductResponseDto product1 = mock(ProductResponseDto.class);
        ProductResponseDto product2 = mock(ProductResponseDto.class);
        expectedProducts.add(product1);
        expectedProducts.add(product2);
        doReturn(expectedProducts).when(productService).getAllProducts();
        ResponseEntity<List<ProductResponseDto>> result = productController.getAllProducts();
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody(), is(equalTo(expectedProducts)));
        verify(productService, atLeast(1)).getAllProducts();
    }

    @Test
    public void testGetProductById() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductResponseDto expectedProduct = mock(ProductResponseDto.class);
        doReturn(expectedProduct).when(productService).getProductById(productId);
        ResponseEntity<ProductResponseDto> result = productController.getProductById(productId);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody(), is(equalTo(expectedProduct)));
        verify(productService, atLeast(1)).getProductById(productId);
    }

    @Test
    public void testCreateProduct() {
        ProductEntity productEntity = mock(ProductEntity.class);
        ProductResponseDto expectedResponse = mock(ProductResponseDto.class);
        doReturn(expectedResponse).when(productService).createProduct(productEntity);
        ResponseEntity<ProductResponseDto> result = productController.createProduct(productEntity);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody(), is(equalTo(expectedResponse)));
        verify(productService, atLeast(1)).createProduct(productEntity);
    }

    @Test
    public void testUpdateProduct() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductEntity productEntity = mock(ProductEntity.class);
        ProductResponseDto expectedResponse = mock(ProductResponseDto.class);
        doReturn(expectedResponse).when(productService).updateProduct(productId, productEntity);
        ResponseEntity<ProductResponseDto> result = productController.updateProduct(productId, productEntity);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody(), is(equalTo(expectedResponse)));
        verify(productService, atLeast(1)).updateProduct(productId, productEntity);
    }

    @Test
    public void testDeleteProductById() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        doNothing().when(productService).deleteProduct(productId);
        ResponseEntity<Void> result = productController.deleteProduct(productId);
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(productService, atLeast(1)).deleteProduct(productId);
    }

    @Test
    public void testDeleteProductByEntity() {
        UUID productId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        ProductEntity productEntity = mock(ProductEntity.class);
        doReturn(productId).when(productEntity).getProductId();
        doNothing().when(productService).deleteProduct(productId);
        ResponseEntity<Void> result = productController.deleteProduct(productEntity);
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(productService, atLeast(1)).deleteProduct(productId);
    }

    @Test
    public void testSearchProducts() {
        String query = "test query";
        List<ProductResponseDto> expectedProducts = new ArrayList<>();
        ProductResponseDto product1 = mock(ProductResponseDto.class);
        expectedProducts.add(product1);
        doReturn(expectedProducts).when(productService).searchProducts(query);
        ResponseEntity<List<ProductResponseDto>> result = productController.searchProducts(query);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody(), is(equalTo(expectedProducts)));
        verify(productService, atLeast(1)).searchProducts(query);
    }

    @Test
    public void testGetProductBySkuSuccess() {
        String sku = "TEST-SKU-001";
        ProductEntity expectedProduct = mock(ProductEntity.class);
        doReturn(Optional.of(expectedProduct)).when(productRepository).findBySku(sku);
        ResponseEntity<ProductEntity> result = productController.getProductBySku(sku);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertThat(result.getBody(), is(equalTo(expectedProduct)));
        verify(productRepository, atLeast(1)).findBySku(sku);
    }

    @Test
    public void testGetProductBySkuNotFound() {
        String sku = "NON-EXISTENT-SKU";
        doReturn(Optional.empty()).when(productRepository).findBySku(sku);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            productController.getProductBySku(sku);
        });
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertThat(exception.getReason(), is(equalTo("SKU not found")));
        verify(productRepository, atLeast(1)).findBySku(sku);
    }

    @Test
    public void testConstructorInitialization() {
        ProductService mockService = mock(ProductService.class);
        ProductController controller = new ProductController(mockService);
        assertNotNull(controller);
    }
}
