package com.example.onlineshop.repository;

import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.ShopQuantity;
import com.example.onlineshop.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopQuantityRepository  extends JpaRepository<ShopQuantity, Long> {

    @Query(value ="SELECT s FROM ShopQuantity s where s.shoppingCart IN :shoppingCart AND s.product IN :product")
    ShopQuantity findByShoppingCart(ShoppingCart shoppingCart, Product product);

    @Query(value ="SELECT s FROM ShopQuantity s where s.shoppingCart.id IN :id AND s.product IN :product")
    ShopQuantity findByShoppingCartId(Long id, Product product);
}
