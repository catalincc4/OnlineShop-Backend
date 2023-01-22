package com.example.onlineshop.repository;

import com.example.onlineshop.model.ShoppingCart;
import com.example.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query(value ="SELECT s FROM ShoppingCart s where s.user IN :user")
    ShoppingCart findByUser(User user);
    @Query("SELECT s from ShoppingCart s where s.id in :id")
    ShoppingCart findByM(Long id);
}
