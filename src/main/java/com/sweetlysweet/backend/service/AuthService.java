package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.dto.*;
import com.sweetlysweet.backend.entity.User;
import com.sweetlysweet.backend.repository.UserRepository;
import com.sweetlysweet.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .role("CUSTOMER")
                .build();
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return buildResponse(token, user);
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash()))
            throw new RuntimeException("Invalid credentials");

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return buildResponse(token, user);
    }

    private AuthResponse buildResponse(String token, User user) {
        return new AuthResponse(token,
                new AuthResponse.UserDto(
                        user.getId(), user.getName(),
                        user.getEmail(), user.getPhone(), user.getRole()));
    }
}