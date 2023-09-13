package com.cartservice.cartservice.cart;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by e069906 on 2/5/2019.
 */
public interface CartRepository extends CrudRepository<Cart,Long> {
}
