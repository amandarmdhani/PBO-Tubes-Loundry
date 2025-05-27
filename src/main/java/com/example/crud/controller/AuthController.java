package com.example.crud.controller;

import com.example.crud.Model.User;
import com.example.crud.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Pastikan role tidak null dan valid, jika null default ke USER
        if(user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        } else {
            // convert role ke uppercase supaya konsisten (optional)
            user.setRole(user.getRole().toUpperCase());
        }

        userRepository.save(user);
        return "redirect:/login";
    }
}
