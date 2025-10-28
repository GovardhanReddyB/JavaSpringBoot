package org.govardhan.userregistrationservice.service;

import org.govardhan.userregistrationservice.DTO.UserRequest;
import org.govardhan.userregistrationservice.DTO.UserResponse;
import org.govardhan.userregistrationservice.exception.UserAlreadyExistException;
import org.govardhan.userregistrationservice.exception.UserNotFoundException;
import org.govardhan.userregistrationservice.model.User;
import org.govardhan.userregistrationservice.repository.UserRepository;
import org.govardhan.userregistrationservice.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService  {
    @Autowired
    private UserRepository userRepository;

    public UserResponse registerUSer(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExistException("User with email " + userRequest.getEmail() + " already exists.");
        }

        User user = new User();
        user.setUsername(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());

        userRepository.save(user);
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), "User registered successfully");
    }

    public String loginUser(UserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.getEmail());
        if (user == null) {
            throw new UserNotFoundException("User with email " + userRequest.getEmail() + " not found.");
        }
        // TODO: Add password verification logic here

        return new JwtService().generateToken(userRequest.getEmail());
    }

    public UserResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User with email " + email + " not found.");
        }

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), "Current User details.");
    }
}
