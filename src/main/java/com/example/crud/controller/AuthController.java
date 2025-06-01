package com.example.crud.controller;

import com.example.crud.Model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Halaman utama (landing page sebelum login)
    @GetMapping("/")
    public String tampilanUtama() {
        return "tampilanUtama"; // pastikan file tampilanUtama.html ada di /resources/templates/
    }

    // Tampilkan halaman login
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    // Tampilkan halaman register
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Proses registrasi
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        System.out.println("User submitted: " + user.getUsername() + ", " + user.getName());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        } else {
            user.setRole(user.getRole().toUpperCase());
        }

        userRepository.save(user);
        return "redirect:/login";
    }

    // Proses login
    @PostMapping("/login")
    public String loginUser(@ModelAttribute User formUser, Model model) {
        User user = userRepository.findByUsername(formUser.getUsername());

        if (user != null && passwordEncoder.matches(formUser.getPassword(), user.getPassword())) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("role", user.getRole());
            return "dashboard";
        }

        model.addAttribute("error", "Username atau password salah");
        return "login";
    }
}
