package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.entity.User;
import com.sweetlysweet.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/me")
    public ResponseEntity<?> update(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Map<String, String> body
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (body.containsKey("name"))
            user.setName(body.get("name"));

        if (body.containsKey("phone"))
            user.setPhone(body.get("phone"));

        // NEW ADDRESS FIELDS

        if (body.containsKey("street"))
            user.setStreet(body.get("street"));

        if (body.containsKey("city"))
            user.setCity(body.get("city"));

        if (body.containsKey("state"))
            user.setState(body.get("state"));

        if (body.containsKey("pincode"))
            user.setPincode(body.get("pincode"));

        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "street", user.getStreet() != null ? user.getStreet() : "",
                "city", user.getCity() != null ? user.getCity() : "",
                "state", user.getState() != null ? user.getState() : "",
                "pincode", user.getPincode() != null ? user.getPincode() : "",
                "role", user.getRole()
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "phone", user.getPhone() != null ? user.getPhone() : "",
                "street", user.getStreet() != null ? user.getStreet() : "",
                "city", user.getCity() != null ? user.getCity() : "",
                "state", user.getState() != null ? user.getState() : "",
                "pincode", user.getPincode() != null ? user.getPincode() : "",
                "role", user.getRole()
        ));
    }
}