package com.example.crud.controller;

import com.example.crud.Model.Order;
import com.example.crud.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String listOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        List<Order> orders = orderRepository.findByCustomerName(username);
        model.addAttribute("orders", orders);
        return "orders";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Order order = orderRepository.findById(id).orElse(null);
        
        if (order == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pesanan tidak ditemukan");
            return "redirect:/orders";
        }

        if (!order.getCustomerName().equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Anda tidak memiliki akses untuk membatalkan pesanan ini");
            return "redirect:/orders";
        }

        if (!"PENDING".equals(order.getStatus())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hanya pesanan dengan status PENDING yang dapat dibatalkan");
            return "redirect:/orders";
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        
        redirectAttributes.addFlashAttribute("successMessage", "Pesanan berhasil dibatalkan");
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Order order = orderRepository.findById(id).orElse(null);
        
        if (order == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pesanan tidak ditemukan");
            return "redirect:/orders";
        }

        if (!order.getCustomerName().equals(username)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Anda tidak memiliki akses untuk menghapus pesanan ini");
            return "redirect:/orders";
        }

        orderRepository.delete(order);
        redirectAttributes.addFlashAttribute("successMessage", "Pesanan berhasil dihapus");
        return "redirect:/orders";
    }
}

@RestController
@RequestMapping("/api/orders")
class OrderApiController {
    private final OrderRepository orderRepository;

    public OrderApiController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) {
        order.setId(id);
        return orderRepository.save(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }
}
