package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.dto.CheckoutRequest;
import com.sweetlysweet.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@AuthenticationPrincipal UserDetails user,
                                      @RequestBody CheckoutRequest req) throws Exception {
        return ResponseEntity.ok(orderService.checkout(user.getUsername(), req));
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> body) throws Exception {
        orderService.verifyPayment(
                Long.valueOf(body.get("orderId").toString()),
                body.get("razorpayOrderId").toString(),
                body.get("razorpayPaymentId").toString(),
                body.get("razorpaySignature").toString()
        );
        return ResponseEntity.ok(Map.of("message", "Payment verified"));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> myOrders(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(orderService.getMyOrders(user.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id,
                                      @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(orderService.getOrderById(id, user.getUsername()));
    }
}