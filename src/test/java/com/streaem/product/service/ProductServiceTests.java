package com.streaem.product.service;

import com.streaem.product.exception.OptimisticLockException;
import com.streaem.product.model.Product;
import com.streaem.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productRepository);
    }


    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );

        Mockito.when(productRepository.getAllProducts()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();
        assertThat(result, hasSize(2));
        assertThat(result.get(0).getId(), is(1L));
        assertThat(result.get(0).getName(), is("Product 1"));
        assertThat(result.get(1).getId(), is(2L));
        assertThat(result.get(1).getName(), is("Product 2"));
    }

    @Test
    void testGetProductById() {
        Long productId = 1L;
        Product product = new Product().setId(productId).setName("Product 1");

        Mockito.when(productRepository.getProductById(productId)).thenReturn(product);

        Product result = productService.getProductById(productId);
        assertThat(result, notNullValue());
        assertThat(result.getId(), is(productId));
        assertThat(result.getName(), is("Product 1"));
    }

    @Test
    void testGetProductsByCategory_InStockOnly() {
        String category = "Electronics";
        int page = 0;
        int size = 10;
        boolean inStockOnly = true;

        Pageable pageable = PageRequest.of(page, size);
        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );
        Page<Product> productPage = new PageImpl<>(productList, pageable, 2);

        Mockito.when(productRepository.findInStockProductsByCategory(category, page * size, size)).thenReturn(productList);
        Mockito.when(productRepository.countProductsByCategory(category, inStockOnly)).thenReturn(2L);

        Page<Product> result = productService.getProductsByCategory(category, pageable, inStockOnly);
        assertThat(result, notNullValue());
        assertThat(result.getContent(), hasSize(2));
        assertThat(result.getContent().get(0).getId(), is(1L));
        assertThat(result.getContent().get(0).getName(), is("Product 1"));
        assertThat(result.getContent().get(1).getId(), is(2L));
        assertThat(result.getContent().get(1).getName(), is("Product 2"));
        assertThat(result.getTotalElements(), is(2L));
    }

    @Test
    void testGetProductsByCategory_AllProducts() {
        String category = "Electronics";
        int page = 0;
        int size = 10;
        boolean inStockOnly = false;

        Pageable pageable = PageRequest.of(page, size);
        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );

        Mockito.when(productRepository.findProductsByCategory(category, page * size, size)).thenReturn(productList);
        Mockito.when(productRepository.countProductsByCategory(category, inStockOnly)).thenReturn(2L);

        Page<Product> result = productService.getProductsByCategory(category, pageable, inStockOnly);
        assertThat(result, notNullValue());
        assertThat(result.getContent(), hasSize(2));
        assertThat(result.getContent().get(0).getId(), is(1L));
        assertThat(result.getContent().get(0).getName(), is("Product 1"));
        assertThat(result.getContent().get(1).getId(), is(2L));
        assertThat(result.getContent().get(1).getName(), is("Product 2"));
        assertThat(result.getTotalElements(), is(2L));
    }

    @Test
    void testUpdateProduct() {
        Long productId = 1L;
        Product updatedProduct = new Product().setId(productId).setName("Updated Product");
        Product existingProduct = new Product().setId(productId).setName("Existing Product");

        Mockito.when(productRepository.getProductById(productId)).thenReturn(existingProduct);
        Mockito.when(productRepository.updateProduct(Mockito.any(Product.class))).thenReturn(1);

        productService.updateProduct(updatedProduct);

        assertThat(existingProduct.getName(), is("Updated Product"));

        Mockito.verify(productRepository, Mockito.times(1)).updateProduct(existingProduct);
    }


    @Test
    void testUpdateProduct_ConcurrentModification() {
        Long productId = 1L;
        Product updatedProduct = new Product().setId(productId).setName("Updated Product");
        Product existingProduct = new Product().setId(productId).setName("Existing Product").setVersion(1);

        Mockito.when(productRepository.getProductById(productId)).thenReturn(existingProduct);

        assertThrows(OptimisticLockException.class, () -> {
            productService.updateProduct(updatedProduct);
        });
    }

    @Test
    void testUpdateStockLevel() {
        Long productId = 1L;
        Product updatedProduct = new Product().setId(productId).setStock(10);
        Product existingProduct = new Product().setId(productId).setStock(5);

        Mockito.when(productRepository.getProductById(productId)).thenReturn(existingProduct);
        Mockito.when(productRepository.updateStockLevel(existingProduct)).thenReturn(1);

        productService.updateStockLevel(updatedProduct);

        assertThat(existingProduct.getStock(), is(10));

        Mockito.verify(productRepository, Mockito.times(1)).updateStockLevel(existingProduct);
    }

    @Test
    void testUpdateStockLevel_ConcurrentModification() {
        Long productId = 1L;
        Product updatedProduct = new Product().setId(productId).setStock(10);
        Product existingProduct = new Product().setId(productId).setStock(5).setVersion(1);

        Mockito.when(productRepository.getProductById(productId)).thenReturn(existingProduct);

        assertThrows(OptimisticLockException.class, () -> {
            productService.updateStockLevel(updatedProduct);
        });
    }
}
