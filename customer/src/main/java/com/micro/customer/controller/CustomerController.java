package com.micro.customer.controller;
import com.micro.customer.feign_client.ProductClient;
import com.micro.customer.feign_client.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final ProductClient productClient;


    @Value("${customer.service.greeting}")
    private String greeting;

    @GetMapping("/hello")
    public String sayHello() {
        return greeting;
    }

    @GetMapping("/products")
    public List<ProductDTO> products(){
        return productClient.products();
    }

}
