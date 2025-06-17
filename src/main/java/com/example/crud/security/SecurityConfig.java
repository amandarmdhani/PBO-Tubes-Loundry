package com.example.crud.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    public SecurityConfig(CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http)) // Menggunakan konfigurasi CORS di bawah
            .csrf(csrf -> csrf.disable()) // Nonaktifkan CSRF, umum untuk API
            .authorizeHttpRequests(auth -> auth
                // PERBAIKAN: Izinkan akses ke semua file statis dan halaman publik
                .requestMatchers(
                    "/", 
                    "/tampilanUtama", 
                    "/register", 
                    "/login", 
                    "/css/**", // Memperbaiki path agar mencakup semua file css
                    "/js/**",   // Memperbaiki path agar mencakup semua file js
                    "/img/**"   // Menambahkan path untuk gambar
                ).permitAll()
                // PERBAIKAN: Mengizinkan akses ke endpoint API untuk pemesanan
                .requestMatchers("/api/orders/**").permitAll() 
                .requestMatchers("/admin/**", "/dashboardadmin").hasRole("ADMIN")
                .requestMatchers("/dashboard", "/profil").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customLoginSuccessHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // PERBAIKAN: Terapkan CORS ke semua path (/**) bukan hanya root (/)
                registry.addMapping("/**") 
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
                        .allowCredentials(true);
            }
        };
    }
}
