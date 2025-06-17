package com.example.crud.controller;



import com.example.crud.Model.User;

import com.example.crud.repository.UserRepository;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller

public class ProfileController {



    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;

    }



    @GetMapping("/profil")

    public String profil(Model model, RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {

            redirectAttributes.addFlashAttribute("errorMessage", "Silakan login terlebih dahulu.");

            return "redirect:/login";

        }



        String username = auth.getName();

        User user = userRepository.findByUsername(username);



        if (user == null) {

            redirectAttributes.addFlashAttribute("errorMessage", "Data pengguna tidak ditemukan.");

            return "redirect:/dashboard";

        }



        model.addAttribute("user", user);

        return "profil";

    }



    @PostMapping("/profil")

    public String updateProfile(@ModelAttribute User updatedUser, RedirectAttributes redirectAttributes) {

        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String username = auth.getName();

            User currentUser = userRepository.findByUsername(username);



            if (currentUser == null) {

                redirectAttributes.addFlashAttribute("errorMessage", "Data pengguna tidak ditemukan.");

                return "redirect:/dashboard";

            }



            // Update data nama & email

            currentUser.setName(updatedUser.getName());

            currentUser.setEmail(updatedUser.getEmail());



            // Jika password baru tidak kosong, update dan hash password

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {

                currentUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));

            }



            userRepository.save(currentUser);

            redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui.");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui profil: " + e.getMessage());

        }



        return "redirect:/profil";

    }

}

