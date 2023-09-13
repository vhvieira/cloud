package com.cartservice.cartservice.product;

import com.cartservice.cartservice.cart.Cart;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by e069906 on 2/4/2019.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localId;

    private Long id;

    private String name;

    private String description;

    private Double cost;

    private Double price;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "cart_id", referencedColumnName = "id")
    private Cart cart;

}
