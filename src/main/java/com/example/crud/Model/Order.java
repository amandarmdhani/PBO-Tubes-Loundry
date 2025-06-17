package com.example.crud.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

// PERBAIKAN 1: Menambahkan ini untuk mengatasi error Lazy Loading dari Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String service;
    private int kilogram;
    private double deliveryCost;
    private String paymentMethod;
    private String paymentDetail;
    private double totalPrice;
    private String customerName;
    private String status = "PENDING";
    private String notes;


    // PERBAIKAN 2: Menambahkan @JsonIgnore untuk mencegah Jackson mencoba
    // mengubah objek User yang menyebabkan Lazy Loading Exception.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // Secara eksplisit set ke LAZY (praktik terbaik)
    @JoinColumn(name = "user_id")
    private User user;

    // PERBAIKAN 3: Menyederhanakan tanggal, hanya menggunakan LocalDate
    // dan menginisialisasi nilainya saat objek dibuat.
    private LocalDate orderDate = LocalDate.now();

    // Getter dan Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getKilogram() {
        return kilogram;
    }

    public void setKilogram(int kilogram) {
        this.kilogram = kilogram;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(String paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

} 