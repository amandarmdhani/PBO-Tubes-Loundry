package com.example.crud.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

@GetMapping("/dashboard")
public String dashboard() {
    return "dashboardUser";  // Hanya untuk user biasa
}

@GetMapping("/dashboardadmin")
public String dashboardAdmin() {
    return "dashboardAdmin";  // Hanya untuk admin
}

}