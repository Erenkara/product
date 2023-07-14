package com.streaem.product.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductUpdateException.class)
    public ResponseEntity<Object> handleProductUpdateException(ProductUpdateException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<Object> handleOptimisticLockException(OptimisticLockException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}