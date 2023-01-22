package com.example.onlineshop.model;

import java.util.List;

public class Result {

    private ShoppingCart shoppingCart;
    private List<Integer> list;

    public Result(ShoppingCart shoppingCart, List<Integer> list) {
        this.shoppingCart = shoppingCart;
        this.list = list;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }
}
