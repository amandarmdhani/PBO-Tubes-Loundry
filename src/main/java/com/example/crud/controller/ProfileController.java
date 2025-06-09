package com.example.crud.controller;

import com.example.crud.Model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {
    
    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profil")
    public String profil(Model model, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Silakan login terlebih dahulu");
            return "redirect:/login";
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Data user tidak ditemukan");
            return "redirect:/dashboard";
        }

        model.addAttribute("user", user);
        return "profil";
    }
}