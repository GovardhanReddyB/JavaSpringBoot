package org.govardhan.userregistrationservice.controller;

import jakarta.validation.Valid;
import org.govardhan.userregistrationservice.DTO.UserRequest;
import org.govardhan.userregistrationservice.DTO.UserResponse;
import org.govardhan.userregistrationservice.model.User;
import org.govardhan.userregistrationservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse response = userService.registerUSer(userRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserRequest loginRequest) {
        System.out.printf("Login attempt for email: %s%n", loginRequest.getEmail());
        String token = userService.loginUser(loginRequest);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("profile")
    public ResponseEntity<UserResponse> getUserProfile(Authentication authentication) {
        String email = authentication.getName();
        UserResponse userResponse = userService.getUserProfile(email);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("health")
    public String healthCheck() {
        return "User Registration Service is up and running!";
    }
}
