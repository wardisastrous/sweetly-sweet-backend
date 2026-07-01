package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.dto.AuthRequest;
import com.sweetlysweet.backend.dto.RegisterRequest;
import com.sweetlysweet.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest req) {

        return ResponseEntity.ok(
                authService.register(req)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthRequest req) {

        return ResponseEntity.ok(
                authService.login(req)
        );
    }
}