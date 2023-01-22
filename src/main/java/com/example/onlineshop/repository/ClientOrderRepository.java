package com.example.onlineshop.repository;

import com.example.onlineshop.model.ClientOrder;

import com.example.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
    @Query("SELECT s from ClientOrder s where  s.user.id =?1")
    List<ClientOrder> findAllByM(Long id);
}
