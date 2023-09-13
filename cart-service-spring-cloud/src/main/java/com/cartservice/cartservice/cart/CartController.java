package com.cartservice.cartservice.cart;

import com.cartservice.cartservice.product.Product;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by e069906 on 2/4/2019.
 */
@RestController
@RequestMapping("/carts")
@NoArgsConstructor
@AllArgsConstructor
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCartById(@PathVariable Long id){
        return ResponseEntity.of(cartService.getCart(id));
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(){
       return ResponseEntity.of(Optional.of(cartService.createCart()));
    }

    @PostMapping("/{id}/products")
    public ResponseEntity<Cart> addProduct(@PathVariable Long id, @RequestBody Product product){
        return ResponseEntity.of(Optional.of(cartService.addProduct(id, product)));
    }
}
