package com.micro.customer.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/users")
public class UserController {

    @PostMapping
    public String create() {
        return "User created successfully!";
    }

    @GetMapping
    public String read() {
        return "User read successfully!";
    }

    @PutMapping("/{id}")
    public String edit(@PathVariable String id) {
        return "User edit successfully!";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        return "User delete successfully!";
    }

}
