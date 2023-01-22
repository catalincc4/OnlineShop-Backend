package com.example.onlineshop.controller;

import com.example.onlineshop.exception.OrderNotFoundException;
import com.example.onlineshop.model.*;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ClientOrderRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.repository.ShopQuantityRepository;
import com.example.onlineshop.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000/")
public class ClientOrderController {

    @Autowired
    private ClientOrderRepository clientOrderRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopQuantityRepository shopQuantityRepository;

    @PostMapping("/order")
    ClientOrder makeNewOrder(@RequestBody ClientOrder newOrder) throws IOException {
        newOrder.setStatus(OrderStatus.PROCESSING);
        return  clientOrderRepository.save(newOrder);
    }

    @GetMapping("/orders")
    List<ClientOrder>  getOrders(){
        return clientOrderRepository.findAll();
    }

    @PutMapping("/orders/{id}")
    List<ClientOrder>  getOrders(@PathVariable Long id){
        return clientOrderRepository.findAllByM(id);
    }


    @PutMapping("/order/{id}")
    ClientOrder updateProduct(@RequestBody ClientOrder newOder, @PathVariable Long id) {
        return clientOrderRepository.findById(id)
                .map(order -> {
                    if(newOder.getStatus() == OrderStatus.PROCESSING){
                    order.setStatus(OrderStatus.SHIPPING);
                    }else{
                        if (newOder.getStatus() == OrderStatus.SHIPPING) {
                            order.setStatus(OrderStatus.DONE);
                        }
                    }
                    return clientOrderRepository.save(order);
                }).orElseThrow(() -> new OrderNotFoundException(id));
    }


    @PostMapping("/pay")
    ClientOrder pay(@RequestBody ShoppingCart shoppingCart) {
       ClientOrder clientOrder = new ClientOrder();
       ShoppingCart shoppingCart1 = shoppingCartRepository.findByM(shoppingCart.getId());
       clientOrder.setNames("");
       for(Product p: shoppingCart1.getProducts() ){
           ShopQuantity shopQuantity = shopQuantityRepository.findByShoppingCart(shoppingCart1, p);
           p.setQuantity(p.getQuantity() - shopQuantity.getQuantity());
           productRepository.save(p);
           clientOrder.setNames(clientOrder.getNames() + p.getName() + ", \n");
       }
       clientOrder.setStatus(OrderStatus.PROCESSING);

       clientOrder.setUser(shoppingCart1.getUser());
       return clientOrderRepository.save(clientOrder);
    }







}
