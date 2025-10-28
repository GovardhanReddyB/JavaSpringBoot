package org.govardhan.userregistrationservice.service;

import org.govardhan.userregistrationservice.DTO.LoginRequest;
import org.govardhan.userregistrationservice.DTO.LoginResponse;
import org.govardhan.userregistrationservice.DTO.UserRequest;
import org.govardhan.userregistrationservice.DTO.UserResponse;
import org.govardhan.userregistrationservice.exception.InvalidCredentialsException;
import org.govardhan.userregistrationservice.exception.UserAlreadyExistException;
import org.govardhan.userregistrationservice.exception.UserNotFoundException;
import org.govardhan.userregistrationservice.model.User;
import org.govardhan.userregistrationservice.repository.UserRepository;
import org.govardhan.userregistrationservice.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUSer(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistException("User with email " + userRequest.getEmail() + " already exists.");
        }

        User user = new User();
        user.setUsername(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), "User registered successfully");
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if (user == null) {
            throw new InvalidCredentialsException("User with email " + loginRequest.getEmail() + " not found.");
        }
        // Todo: Add password verification logic here
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid password for email " + loginRequest.getEmail() + ".");
        }
        String token = new JwtService().generateToken(loginRequest.getEmail());
        return new LoginResponse(token, "Login successful");
    }

    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), "Current User details.");
    }
}
