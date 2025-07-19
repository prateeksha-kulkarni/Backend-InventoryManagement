package com.storeInventory.inventory_management.repository;

import com.storeInventory.inventory_management.auth.model.Enum.ProductCategory;
import com.storeInventory.inventory_management.auth.model.ProductEntity;
import com.storeInventory.inventory_management.auth.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    public void whenSavedProduct_thenReturnNonNullProduct() {
        ProductEntity product = new ProductEntity();
        product.setName("Test Product");
        product.setSku("SKU123");
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setDescription("A test product");

        ProductEntity savedProduct = productRepository.save(product);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals("Test Product", savedProduct.getName());
        Assertions.assertEquals("SKU123", savedProduct.getSku());
        Assertions.assertEquals(ProductCategory.ELECTRONICS, savedProduct.getCategory());
    }
} 