package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(cartService.getCart(user.getUsername()));
    }

    @PostMapping
    public ResponseEntity<?> add(@AuthenticationPrincipal UserDetails user,
                                 @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int  quantity  = Integer.parseInt(body.getOrDefault("quantity", 1).toString());
        return ResponseEntity.ok(cartService.addToCart(user.getUsername(), productId, quantity));
    }

    @PutMapping
    public ResponseEntity<?> update(@AuthenticationPrincipal UserDetails user,
                                    @RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        int  quantity  = Integer.parseInt(body.get("quantity").toString());
        return ResponseEntity.ok(cartService.updateQuantity(user.getUsername(), productId, quantity));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> remove(@AuthenticationPrincipal UserDetails user,
                                    @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(user.getUsername(), productId));
    }
}