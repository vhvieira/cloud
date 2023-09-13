package com.cartservice.cartservice.product;

import lombok.Data;

/**
 * Created by e069906 on 2/7/2019.
 */
@Data
public class ExternalProduct {
    Long id;
    String name;
    String description;
    Double price;
    Double cost;
}