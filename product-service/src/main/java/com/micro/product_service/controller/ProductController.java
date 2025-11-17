package com.micro.product_service.controller;

import com.micro.product_service.feign_client.CustomerClient;
import com.micro.product_service.feign_client.CustomerDTO;
import com.micro.product_service.model.Product;
import com.micro.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;
    private final CustomerClient customerClient;

    @Value("${product.service.greeting}")
    private String greeting;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping("/greeting")
    public String greeting(){
        return greeting;
    }

    @GetMapping("/read-customer")
    public List<CustomerDTO> customer(){
        return customerClient.read();
    }
}

