package com.example.onlineshop.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Could not found the product with id " + id);
    }
}
