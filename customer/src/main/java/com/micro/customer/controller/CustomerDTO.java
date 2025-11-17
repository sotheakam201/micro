package com.micro.customer.controller;

import lombok.Builder;

@Builder
public record CustomerDTO(
        Integer id,
        String name
) {
}
