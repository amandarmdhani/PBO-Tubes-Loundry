package com.example.crud.service;

import com.example.crud.Model.Order;
import com.example.crud.Model.User;
import com.example.crud.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    @Transactional
    public Order createOrder(Order order, User user) {
        if (user == null) {
            throw new RuntimeException("User tidak ditemukan");
        }
        order.setUser(user);
        order.setCustomerName(user.getName());
        order.setStatus("PENDING");
        return orderRepository.save(order);
    }

    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan dengan id: " + id));
    }

    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        Order existingOrder = findOrderById(id); // Gunakan metode internal untuk mencari

        existingOrder.setOrderDate(orderDetails.getOrderDate());
        existingOrder.setCustomerName(orderDetails.getCustomerName());
        existingOrder.setService(orderDetails.getService());
        existingOrder.setKilogram(orderDetails.getKilogram());
        existingOrder.setTotalPrice(orderDetails.getTotalPrice());
        existingOrder.setStatus(orderDetails.getStatus());
        existingOrder.setDeliveryCost(orderDetails.getDeliveryCost());
        existingOrder.setPaymentMethod(orderDetails.getPaymentMethod());
        existingOrder.setPaymentDetail(orderDetails.getPaymentDetail());
        existingOrder.setNotes(orderDetails.getNotes());

        return orderRepository.save(existingOrder);
    }

    @Transactional
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    @Transactional
    public Order updateOrderStatus(Long id, String status) {
        Order order = findOrderById(id);
        order.setStatus(status.toUpperCase());
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long id) {
        Order order = findOrderById(id);
        order.setStatus("Dibatalkan"); // Anda bisa menggunakan enum untuk status
        orderRepository.save(order);
    }
}