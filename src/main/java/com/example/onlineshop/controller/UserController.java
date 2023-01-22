package com.example.onlineshop.controller;


import com.example.onlineshop.exception.UserNotFoundException;
import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.ShoppingCartRepository;
import com.example.onlineshop.repository.SupplierRepository;
import com.example.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encoderPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encoderPassword);
       // newUser.setUserType(UserType.CLIENT);
        return userRepository.save(newUser);
    }

    @PostMapping("/user/admin")
    String newUserByAdmin(@RequestBody User newUser){
        if(!newUser.getUserType().equals(UserType.ADMIN)){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encoderPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encoderPassword);
            if(userRepository.save(newUser) != null){
                User user1 = userRepository.findByEmail(newUser.getEmail());
                if(newUser.getUserType().equals(UserType.SUPPLIER)) {
                    Supplier supplier = new Supplier(new ArrayList<>(), user1);
                    supplierRepository.save(supplier);
                    return "succes";
                }
                if(newUser.getUserType().equals(UserType.CLIENT)) {
                    ShoppingCart shoppingCart = new ShoppingCart(user1, new ArrayList<>());
                    shoppingCartRepository.save(shoppingCart);
                    return "succes";
                }
            }
        }
        return "fail";
    }


    @PostMapping("/connect")
    public User connectToAccount(@RequestBody User user){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user1= userRepository.findByEmail(user.getEmail());
        if(user1 != null){
            CharSequence charSequence = user.getPassword().subSequence(0, user.getPassword().length());
            if(passwordEncoder.matches(charSequence, user1.getPassword())){
               return user1;
            }
            return null;
        }
        return null;
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
       return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @DeleteMapping("/user/delete/{id}")
    String deleteUser(@PathVariable Long id){
        if (!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }else{
             userRepository.deleteById(id);
             return "User with id " + id + " has been deleted";
        }
    }


}
