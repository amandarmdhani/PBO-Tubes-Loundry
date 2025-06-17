package com.example.crud.controller;

import com.example.crud.Model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            logger.debug("Processing registration for user: {}", user.getUsername());
            
            if (userRepository.existsByUsername(user.getUsername())) {
                logger.debug("Username {} already exists", user.getUsername());
                
                // --- PENYEMPURNAAN DI SINI ---
                // Kirim kembali data user yang sudah diinput agar form tidak kosong
                redirectAttributes.addFlashAttribute("user", user); 
                redirectAttributes.addFlashAttribute("error", "Username sudah terdaftar, silakan gunakan username lain.");
                
                return "redirect:/register";
            }

            // Log user data before saving
            logger.debug("User data before saving - Name: {}, Username: {}, Email: {}", 
                user.getName(), user.getUsername(), user.getEmail());

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole("USER");
            
            User savedUser = userRepository.save(user);
            logger.debug("User saved successfully with ID: {}", savedUser.getId());

            redirectAttributes.addFlashAttribute("success", "Registrasi berhasil, silakan login.");
            return "redirect:/login";
        } catch (Exception e) {
            logger.error("Error during registration: ", e);
            redirectAttributes.addFlashAttribute("error", "Terjadi kesalahan saat registrasi: " + e.getMessage());
            return "redirect:/register";
        }
    }
}