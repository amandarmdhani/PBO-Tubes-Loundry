package com.example.crud.repository;

import com.example.crud.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();
    List<Product> findByTypeAndActiveTrue(String type);
    List<Product> findByCategoryAndActiveTrue(String category);
}  