package com.micro.customer.feign_client;

import lombok.Builder;

@Builder
public record ProductDTO(
        Integer id,
        String name
) {
}
