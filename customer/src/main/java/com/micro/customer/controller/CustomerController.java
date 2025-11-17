package com.micro.customer.controller;
import com.micro.customer.feign_client.ProductClient;
import com.micro.customer.feign_client.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private ProductClient productClient;

    @Value("${customer.service.greeting}")
    private String greeting;

    @GetMapping("/hello")
    public String sayHello() {
        return greeting;
    }

    @GetMapping("/products")
    public List<ProductDTO> products(){
        System.out.println("11111111111111111111111111111111");
        return  productClient.products();
    }
}
