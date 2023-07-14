package com.streaem.product.controller;

import com.streaem.product.controller.ProductController;
import com.streaem.product.exception.OptimisticLockException;
import com.streaem.product.model.Product;
import com.streaem.product.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.streaem.product.model.Product;
import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    // Helper method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testGetProductById() throws Exception {
        Long productId = 1L;
        Product product = new Product().setId(productId).setName("Product 1");

        Mockito.when(productService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/products/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(productId.intValue())))
                .andExpect(jsonPath("$.name", is("Product 1")));
    }

    @Test
    public void testGetProductsByCategory() throws Exception {
        String category = "Electronics";
        int page = 0;
        int size = 10;
        boolean inStockOnly = true;

        Pageable pageable = PageRequest.of(page, size);
        List<Product> productList = Arrays.asList(new Product().setId(1L).setName("Product 1"), new Product().setId(2L).setName("Product 2"));
        Page<Product> productPage = new PageImpl<>(productList, pageable, 2);

        Mockito.when(productService.getProductsByCategory(category, pageable, inStockOnly)).thenReturn(productPage);

        mockMvc.perform(get("/products/{category}", category)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("inStockOnly", String.valueOf(inStockOnly)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("Product 1")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Product 2")));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Long productId = 1L;
        Product updatedProduct = new Product().setId(productId).setName("Updated Product");

        mockMvc.perform(put("/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedProduct)))
                .andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).updateProduct(updatedProduct);
    }

    @Test
    public void testUpdateStockLevel() throws Exception {
        Long productId = 1L;
        Product updatedProduct = new Product().setId(productId).setStock(10);

        mockMvc.perform(put("/products/{productId}/stock", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedProduct)))
                .andExpect(status().isNoContent());

        Mockito.verify(productService, Mockito.times(1)).updateStockLevel(updatedProduct);
    }
}
