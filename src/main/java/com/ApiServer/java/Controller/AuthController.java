package com.ApiServer.java.Controller;

import com.ApiServer.java.Model.User;
import com.ApiServer.java.Repository.UserRepository;
import com.ApiServer.java.Security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                logger.warn("Signup failed: User '{}' already exists", user.getUsername());
                throw new ResponseStatusException(BAD_REQUEST, "User already exists");
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            logger.info("User '{}' registered successfully", user.getUsername());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception ex) {
            logger.error("Exception during signup for user '{}': {}", user.getUsername(), ex.getMessage(), ex);
            throw ex;
        }
    }

    // Authenticate user and return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User foundUser = userRepository.findByUsername(user.getUsername());

            if (foundUser == null || !passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                logger.warn("Login failed for user '{}': Invalid credentials", user.getUsername());
                throw new ResponseStatusException(UNAUTHORIZED, "Invalid username or password");
            }

            String token = jwtUtil.generateToken(foundUser.getUsername());

            logger.info("User '{}' logged in successfully", foundUser.getUsername());

            Map<String, String> response = new HashMap<>();
            response.put("token", "Bearer " + token);

            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            logger.error("Exception during login for user '{}': {}", user.getUsername(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
