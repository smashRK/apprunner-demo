package com.Turtle.poccurd.controller;

import com.Turtle.poccurd.model.Product;
import com.Turtle.poccurd.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllUsers() {
        logger.info("GET request received at /api/users");
        return productService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getUserById(@PathVariable Long id) {
        logger.info("GET request received at /api/users/{}", id);
        return productService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> createUser(@RequestBody Product product) {
        logger.info("POST request received at /api/users with product: {}", product);
        try {
            Product savedProduct = productService.createUser(product);
            logger.info("Product saved successfully: {}", savedProduct);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            logger.error("Error creating product: ", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateUser(@PathVariable Long id, @RequestBody Product productDetails) {
        logger.info("PUT request received at /api/users/{} with product: {}", id, productDetails);
        try {
            Product updatedProduct = productService.updateUser(id, productDetails);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            logger.error("Error updating product: ", e);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("DELETE request received at /api/users/{}", id);
        try {
            productService.deleteUser(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            logger.error("Error deleting product: ", e);
            return ResponseEntity.notFound().build();
        }
    }
}