package com.streaem.product.service;

import com.streaem.product.exception.OptimisticLockException;
import com.streaem.product.exception.ProductUpdateException;
import com.streaem.product.model.Product;
import com.streaem.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Product getProductById(Long productId) {
        return productRepository.getProductById(productId);
    }

    public Page<Product> getProductsByCategory(String category, Pageable pageable, boolean inStockOnly) {
        int offset = pageable.getPageNumber() * pageable.getPageSize();
        List<Product> productList;

        if (inStockOnly) {
            productList = productRepository.findInStockProductsByCategory(category, offset, pageable.getPageSize());
        } else {
            productList = productRepository.findProductsByCategory(category, offset, pageable.getPageSize());
        }

        long totalCount = productRepository.countProductsByCategory(category, inStockOnly);

        return new PageImpl<>(productList, pageable, totalCount);
    }

    @Transactional
    public void updateProduct(Product updatedProduct) {
        Product existingProduct = productRepository.getProductById(updatedProduct.getId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (existingProduct.getVersion() != updatedProduct.getVersion()) {
            throw new OptimisticLockException("Failed to update product due to concurrent modification");
        }

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setStock(updatedProduct.getStock());

        int rowsUpdated = productRepository.updateProduct(existingProduct);
        if (rowsUpdated != 1) {
            throw new ProductUpdateException("Failed to update product");
        }
    }


    @Transactional
    public void updateStockLevel(Product updatedProduct) {
        Product existingProduct = productRepository.getProductById(updatedProduct.getId());
        if (existingProduct == null) {
            throw new IllegalArgumentException("Product not found");
        }

        if (existingProduct.getVersion() != updatedProduct.getVersion()) {
            throw new OptimisticLockException("Failed to update product due to concurrent modification");
        }

        existingProduct.setStock(updatedProduct.getStock());

        int rowsAffected = productRepository.updateStockLevel(existingProduct);
        if (rowsAffected != 1) {
            throw new ProductUpdateException("Failed to update stock level");
        }
    }
}


