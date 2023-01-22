package com.example.onlineshop.controller;

import com.example.onlineshop.exception.SupplierNotFoundException;
import com.example.onlineshop.exception.UserNotFoundException;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.Supplier;
import com.example.onlineshop.model.User;
import com.example.onlineshop.repository.SupplierRepository;
import com.example.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin("http://localhost:3000")
public class SupplierController {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/supplier")
    Supplier newSupplier (@RequestBody Supplier newSupplier){
        return supplierRepository.save(newSupplier);
    }

    @GetMapping("/supplier/user/{id}")
    Supplier getSupplierByUser(@PathVariable Long id){
        User user1 = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        if(user1 != null) {
            Supplier supplier = supplierRepository.findByUser(user1);
            if(supplier != null){
                return supplier;
            }
            return null;
        }
        return null;
    }
//
    @PostMapping("/supplierproducts/user")
    List<Product> getProductsSupplierByUser(@RequestBody User user){
        User user1 = userRepository.findByEmail(user.getEmail());
        if(user1 != null) {
            Supplier supplier = supplierRepository.findByUser(user1);
            if(supplier != null){
                return supplier.getProducts();
            }
            return null;
        }
        return null;
    }

    @PutMapping("/edit/supplier/{id}")
    Supplier updateProduct(@RequestBody Product product, @PathVariable Long id) {
//       List<Product> products = new ArrayList<>();
        return supplierRepository.findById(id)
                .map(supplier ->{
                    supplier.getProducts().add(product);
                    supplier.setProducts(supplier.getProducts());
                    return supplierRepository.save(supplier);
                }).orElseThrow(() -> new SupplierNotFoundException(id));
    }

    @GetMapping("/sup/{id}")
    Supplier findSupplierById(@PathVariable Long id){
    return supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));
    }


    }
