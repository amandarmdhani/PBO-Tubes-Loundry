package com.example.crud.config; // Pastikan package Anda benar

import com.example.crud.Model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Logika untuk membuat data awal dipindahkan ke sini
        // Metode ini akan berjalan setelah semua bean siap
        
        // Contoh: Membuat admin jika belum ada
        // Memeriksa apakah hasil dari repository adalah null
        if (userRepository.findByUsername("admin") == null) {
    // ...

            User admin = new User();
            admin.setUsername("admin");
            admin.setName("Administrator");
            admin.setPassword(passwordEncoder.encode("password123")); // Ganti dengan password yang aman
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println(">>> Admin user created!");
        }

        // Anda bisa menambahkan data lain di sini
    }
} 