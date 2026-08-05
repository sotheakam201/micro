package com.micro.customer.feign_client;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductDTO(
        Integer id,
        String name,
        String description,
        BigDecimal price
) {
}
