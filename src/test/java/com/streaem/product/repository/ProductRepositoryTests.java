package com.streaem.product.repository;

import com.streaem.product.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
class ProductRepositoryTests {

    @Mock
    private ProductRepository productRepository;

    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );

        Mockito.when(productRepository.getAllProducts()).thenReturn(productList);

        List<Product> result = productRepository.getAllProducts();
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

        Product result = productRepository.getProductById(productId);
        assertThat(result, notNullValue());
        assertThat(result.getId(), is(productId));
        assertThat(result.getName(), is("Product 1"));
    }

    @Test
    void testFindInStockProductsByCategory() {
        String category = "Electronics";
        int offset = 0;
        int size = 10;

        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );

        Mockito.when(productRepository.findInStockProductsByCategory(category, offset, size)).thenReturn(productList);

        List<Product> result = productRepository.findInStockProductsByCategory(category, offset, size);
        assertThat(result, hasSize(2));
        assertThat(result.get(0).getId(), is(1L));
        assertThat(result.get(0).getName(), is("Product 1"));
        assertThat(result.get(1).getId(), is(2L));
        assertThat(result.get(1).getName(), is("Product 2"));
    }

    @Test
    void testFindProductsByCategory() {
        String category = "Electronics";
        int offset = 0;
        int size = 10;

        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );

        Mockito.when(productRepository.findProductsByCategory(category, offset, size)).thenReturn(productList);

        List<Product> result = productRepository.findProductsByCategory(category, offset, size);
        assertThat(result, hasSize(2));
        assertThat(result.get(0).getId(), is(1L));
        assertThat(result.get(0).getName(), is("Product 1"));
        assertThat(result.get(1).getId(), is(2L));
        assertThat(result.get(1).getName(), is("Product 2"));
    }

    @Test
    void testCountProductsByCategory() {
        String category = "Electronics";
        boolean inStockOnly = true;

        long totalCount = 10L;

        Mockito.when(productRepository.countProductsByCategory(category, inStockOnly)).thenReturn(totalCount);

        long result = productRepository.countProductsByCategory(category, inStockOnly);
        assertThat(result, is(totalCount));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product().setId(1L).setName("Product 1");

        int rowsAffected = 1;
        Mockito.when(productRepository.updateProduct(product)).thenReturn(rowsAffected);

        int result = productRepository.updateProduct(product);
        assertThat(result, is(rowsAffected));
    }

    @Test
    void testUpdateStockLevel() {
        Product product = new Product().setId(1L).setStock(10);

        int rowsAffected = 1;
        Mockito.when(productRepository.updateStockLevel(product)).thenReturn(rowsAffected);

        int result = productRepository.updateStockLevel(product);
        assertThat(result, is(rowsAffected));
    }

    @Test
    void testSaveAll() {
        List<Product> productList = Arrays.asList(
                new Product().setId(1L).setName("Product 1"),
                new Product().setId(2L).setName("Product 2")
        );

        productRepository.saveAll(productList);

        Mockito.verify(productRepository, Mockito.times(1)).saveAll(productList);
    }
}
