package com.example.onlineshop.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    private User user;


    @OneToMany
    private List<Product> products;


    public User getUser() {
        return user;
    }

    public ShoppingCart() {
    }

    public ShoppingCart(User user, List<Product> products) {
        this.user = user;
        this.products = products;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
