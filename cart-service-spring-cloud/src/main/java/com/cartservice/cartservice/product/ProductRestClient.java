package com.cartservice.cartservice.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by e069906 on 2/7/2019.
 */
@FeignClient(name="ProductsAPI", url = "${application.products.url}")
public interface ProductRestClient {

    @GetMapping("/products/{id}")
    ExternalProduct getProduct(@PathVariable("id") Long id);
}
