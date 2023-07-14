package com.streaem.product.controller;

import com.streaem.product.exception.OptimisticLockException;
import com.streaem.product.model.Product;
import com.streaem.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Product>> getProductsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "false", required = false) boolean inStockOnly
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getProductsByCategory(category, pageable, inStockOnly);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Object> updateProduct(
            @PathVariable Long productId,
            @RequestBody Product updatedProduct) {
        updatedProduct.setId(productId);
        try {
            productService.updateProduct(updatedProduct);
            return ResponseEntity.noContent().build();
        } catch (OptimisticLockException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{productId}/stock")
    public ResponseEntity<String> updateStockLevel(
            @PathVariable Long productId,
            @RequestBody Product updatedProduct) {
        updatedProduct.setId(productId);
        try {
            productService.updateStockLevel(updatedProduct);
            return ResponseEntity.noContent().build();
        } catch (OptimisticLockException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
