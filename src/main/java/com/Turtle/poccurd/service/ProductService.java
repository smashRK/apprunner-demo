package com.Turtle.poccurd.service;

import com.Turtle.poccurd.model.Product;
import com.Turtle.poccurd.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllUsers() {
        return productRepository.findAll();
    }

    public Optional<Product> getUserById(Long id) {
        return productRepository.findById(id);
    }

    public Product createUser(Product product) {
        // Check if the product already exists
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new RuntimeException("Product already exists");
        }

        // Save and return the new product
        return productRepository.save(product);
    }

    public Product updateUser(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("product not found"));

        product.setProductName(productDetails.getProductName());
        product.setDescription(productDetails.getDescription());
        product.setQuantity(productDetails.getQuantity());

        return productRepository.save(product);
    }

    public void deleteUser(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product is not found"));

        productRepository.delete(product);
    }
}
