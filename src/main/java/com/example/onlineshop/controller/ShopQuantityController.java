package com.example.onlineshop.controller;

import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ShopQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ShopQuantityController {

    @Autowired
    ShopQuantityRepository shopQuantityRepository;

    @PostMapping("/shopquantity/{id}")
    Integer getQuantity(@RequestBody Product product, @PathVariable Long id){
        return shopQuantityRepository.findByShoppingCartId(id, product) .getQuantity();
    }
}
