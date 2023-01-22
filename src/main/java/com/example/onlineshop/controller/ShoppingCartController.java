package com.example.onlineshop.controller;

import com.example.onlineshop.exception.SupplierNotFoundException;
import com.example.onlineshop.model.*;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.ShopQuantityRepository;
import com.example.onlineshop.repository.ShoppingCartRepository;
import com.example.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@CrossOrigin("http://localhost:3000")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShopQuantityRepository shopQuantityRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/shoppingcart/user")
    Result getShoppingCartByUser(@RequestBody User user){
        List<Integer> integers = new ArrayList<>();
        User user1 = userRepository.findByEmail(user.getEmail());
        if(user1 != null) {
            ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user1);
            if(shoppingCart != null){
                for(Product p: shoppingCart.getProducts()){
                   ShopQuantity shopQuantity = shopQuantityRepository.findByShoppingCart(shoppingCart, p);
                   integers.add(shopQuantity.getQuantity());
                }

                return new Result(shoppingCart, integers);
            }
            return null;
        }
        return null;
    }

    @PutMapping("/edit/shoppingcart/{id}")
    ShoppingCart updateProduct(@RequestBody Product product, @PathVariable Long id) {
        AtomicBoolean flag = new AtomicBoolean(true);
        Product product1 = productRepository.findByM(product.getId());
        ShoppingCart shoppingCart1 = shoppingCartRepository.findByM(id);

                      for(Product p : shoppingCart1.getProducts()){
                          System.out.println(p.getId()+"  "+product1.getId());
                          if(p.getId().intValue() == product1.getId().intValue()){
                              flag.set(false);
                             ShopQuantity shopQuantity = shopQuantityRepository.findByShoppingCart(shoppingCart1, product1);
                             shopQuantity.setQuantity(shopQuantity.getQuantity() + 1);
                             shopQuantityRepository.save(shopQuantity);

                          }}

                    if(flag.get()){
                        shoppingCart1.getProducts().add(product1);
                        shopQuantityRepository.save(new ShopQuantity(shoppingCart1, product1, 1));
                    }
                    return shoppingCartRepository.save(shoppingCart1);
    }
//    @PutMapping("/cart/decrease/{id}")
//    ShoppingCart decrease(@RequestBody Product  product, @PathVariable Long id) {
//        AtomicBoolean flag = new AtomicBoolean(true);
//        System.out.println(product);
//        return shoppingCartRepository.findById(id)
//                .map(shoppingCart1 -> {
//                    for(Product p : shoppingCart1.getProducts()){
//                        if(p.getId().equals(product.getId())){
//                            if(p.getQuantity() > 1){
//                            p.setQuantity(p.getQuantity() - 1);
//                            }else{
//                                shoppingCart1.getProducts().remove(p);
//                            }
//                        }}
//                    shoppingCart1.setProducts(shoppingCart1.getProducts());
//                    return shoppingCartRepository.save(shoppingCart1);
//                }).orElseThrow(() -> new SupplierNotFoundException(id));
//    }


    @PutMapping("/cart/decrease/{id}")
    ShoppingCart decrease(@RequestBody Product product, @PathVariable Long id) {
        AtomicBoolean flag = new AtomicBoolean(true);
        Product product1 = productRepository.findByM(product.getId());
        return shoppingCartRepository.findById(id)
                .map(shoppingCart1 -> {
                    for(Product p : shoppingCart1.getProducts()){
                        if(p.getId().intValue() == product1.getId().intValue()){
                            ShopQuantity shopQuantity = shopQuantityRepository.findByShoppingCart(shoppingCart1, p);
                            if(shopQuantity.getQuantity() == 1){
                                shoppingCart1.getProducts().remove(product1);
                                shopQuantityRepository.delete(shopQuantity);
                            }else{
                                shopQuantity.setQuantity(shopQuantity.getQuantity() - 1);
                                shopQuantityRepository.save(shopQuantity);
                            }
                            return shoppingCartRepository.save(shoppingCart1);
                        }}
                    return shoppingCartRepository.save(shoppingCart1);
                }).orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @PutMapping("/cart/clear/{id}")
    ShoppingCart clear(@PathVariable Long id) {
        return shoppingCartRepository.findById(id)
                .map(shoppingCart1 -> {
                    for(Product p: shoppingCart1.getProducts()){
                        ShopQuantity shopQuantity = shopQuantityRepository.findByShoppingCart(shoppingCart1, p);
                        shopQuantityRepository.delete(shopQuantity);
                    }
                    shoppingCart1.getProducts().clear();
                    return shoppingCartRepository.save(shoppingCart1);
                }).orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @PutMapping("/cart/remove/{id}")
    ShoppingCart remove(@RequestBody Product product, @PathVariable Long id) {
        Product product1 = productRepository.findByM(product.getId());
        return shoppingCartRepository.findById(id)
                .map(shoppingCart1 -> {
                    ShopQuantity shopQuantity = shopQuantityRepository.findByShoppingCart(shoppingCart1, product1);
                    shopQuantityRepository.delete(shopQuantity);
                    shoppingCart1.getProducts().remove(product1);
                    return shoppingCartRepository.save(shoppingCart1);
                }).orElseThrow(() -> new SupplierNotFoundException(id));
    }


}
