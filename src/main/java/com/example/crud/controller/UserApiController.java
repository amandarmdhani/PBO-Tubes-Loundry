package com.example.crud.controller;

import com.example.crud.Model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

class UserProfileDTO {
    public Long id;
    public String username;
    public String name;
    public String role;

    public UserProfileDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.role = user.getRole();
    }
}

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return ResponseEntity.ok(new UserProfileDTO(user.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
