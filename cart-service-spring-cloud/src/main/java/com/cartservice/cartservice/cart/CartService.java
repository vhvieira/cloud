package com.cartservice.cartservice.cart;

import com.cartservice.cartservice.product.Product;
import com.cartservice.cartservice.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by e069906 on 2/4/2019.
 */
@Service
public class CartService {

    @Autowired
    private CartRepository repository;

    @Autowired
    private ProductService productService;

    public Optional<Cart> getCart(Long id) {
        return repository.findById(id);
    }

    public Cart createCart() {
        return repository.save(new Cart());
    }

    public Cart addProduct(Long id, Product product) {
        Cart c = repository.findById(id).orElseThrow(() -> new RuntimeException());
        Product p = productService.getProduct(product.getId()).orElseThrow(() -> new RuntimeException());
        p.setCart(c);
        c.getProducts().add(p);
        return repository.save(c);
    }
}
