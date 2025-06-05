package com.example.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfilController {

    @GetMapping("/profil")
    public String showProfilPage() {
        return "profil"; // Mengarah ke templates/profil.html
    }
}
