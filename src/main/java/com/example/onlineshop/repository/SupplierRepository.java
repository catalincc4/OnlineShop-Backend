package com.example.onlineshop.repository;

import com.example.onlineshop.model.Supplier;
import com.example.onlineshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @Query(value ="SELECT s FROM Supplier s where s.user IN :user")
    Supplier findByUser(User user);
}
