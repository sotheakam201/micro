package com.micro.product_service.feign_client;

import com.micro.product_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "customer-service", configuration = FeignClientConfig.class)
public interface CustomerClient {


    @GetMapping("/api/customers")
    public List<CustomerDTO> read();

}
