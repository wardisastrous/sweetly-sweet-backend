package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.dto.CouponApplyRequest;
import com.sweetlysweet.backend.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody CouponApplyRequest req) {
        return ResponseEntity.ok(couponService.apply(req.getCode(), req.getOrderTotal()));
    }
}