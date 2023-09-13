package com.cartservice.cartservice.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Created by e069906 on 2/7/2019.
 */
@Service
public class ProductService {

    @Autowired
    private ProductRestClient restClient;

    public Optional<Product> getProduct(Long id){
        ExternalProduct ext = Optional.ofNullable(restClient.getProduct(id))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Product p = new Product();
        p.setId(ext.getId());
        p.setDescription(ext.getDescription());
        p.setName(ext.getName());
        p.setCost(ext.getCost());
        p.setPrice(ext.getPrice());
        return Optional.of(p);
    }


}
