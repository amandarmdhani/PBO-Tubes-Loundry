package com.example.crud.controller;

import com.example.crud.repository.ProductRepository;
import com.example.crud.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import com.example.crud.Model.Product;
import com.example.crud.Model.Order;

@Controller
public class DashboardController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("products", productRepository.findByActiveTrue());
        return "dashboardUser";
    }

    @GetMapping("/dashboardadmin")
    public String dashboardAdmin(Model model) {
        List<Product> products = productRepository.findAll();
        List<Order> orders = orderRepository.findAll();
        int totalProducts = products.size();
        int totalOrders = orders.size();
        double totalIncome = orders.stream()
            .filter(order -> "COMPLETED".equals(order.getStatus()))
            .mapToDouble(Order::getTotalPrice)
            .sum();
        model.addAttribute("products", products);
        model.addAttribute("orders", orders);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalIncome", totalIncome);
        return "dashboardAdmin";
    }
} 