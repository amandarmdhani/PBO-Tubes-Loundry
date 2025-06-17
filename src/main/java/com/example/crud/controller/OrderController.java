package com.example.crud.controller;

import com.example.crud.Model.Order;
import com.example.crud.Model.User; // Pastikan User di-import
import com.example.crud.repository.UserRepository;
import com.example.crud.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    // PERBAIKAN: Menggunakan Constructor Injection, bukan @Autowired pada field
    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping("/orders")
    public String showOrders(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }
        // PERBAIKAN: Panggil service untuk mendapatkan data
        List<Order> userOrders = orderService.findOrdersByUser(user);
        model.addAttribute("orders", userOrders);
        return "orders";
    }

    @PostMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<?> createOrder(@RequestBody Order order, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        try {
            // PERBAIKAN: Seluruh logika pembuatan order didelegasikan ke service
            User user = userRepository.findByUsername(principal.getName());
            Order createdOrder = orderService.createOrder(order, user);
            return ResponseEntity.ok(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/api/orders")
    @ResponseBody
    public List<Order> getAllOrders() {
        // PERBAIKAN: Panggil service
        return orderService.findAllOrders();
    }

    @GetMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        try {
            // PERBAIKAN: Panggil service dan tangani jika tidak ada
            Order order = orderService.findOrderById(id);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
            // PERBAIKAN: Panggil service yang memiliki @Transactional
            Order updatedOrder = orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            // Jika service melempar error (cth: order tidak ditemukan), kirim 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/api/orders/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        // PERBAIKAN: Panggil service
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build(); // Kirim respons 204 No Content
    }

    @PutMapping("/api/orders/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Status tidak boleh kosong");
        }
        try {
            // PERBAIKAN: Panggil service
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // PERBAIKAN: Panggil service
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pesanan berhasil dibatalkan.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Pesanan tidak ditemukan.");
        }
        return "redirect:/orders";
    }
}