package com.example.crud.repository;

import com.example.crud.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.example.crud.Model.User;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    @Query(value = "SELECT COUNT(*) FROM orders", nativeQuery = true)
    long getTotalOrderCount();

    @Query(value = "SELECT COALESCE(SUM(total_price), 0) FROM orders", nativeQuery = true)
    double getTotalIncome();
}
 