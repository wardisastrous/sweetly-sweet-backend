package com.sweetlysweet.backend.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sweetlysweet.backend.dto.ProductRequest;
import com.sweetlysweet.backend.dto.StockUpdateRequest;
import com.sweetlysweet.backend.entity.Coupon;
import com.sweetlysweet.backend.service.CouponService;
import com.sweetlysweet.backend.service.OrderService;
import com.sweetlysweet.backend.service.ProductService;
import com.sweetlysweet.backend.service.UserService;
import com.sweetlysweet.backend.service.AnalyticsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final UserService userService;
    private final AnalyticsService analyticsService;
    private final Cloudinary cloudinary;

    public AdminController(
            ProductService productService,
            OrderService orderService,
            CouponService couponService,
            UserService userService,
            AnalyticsService analyticsService,
            Cloudinary cloudinary) {

        this.productService = productService;
        this.orderService = orderService;
        this.couponService = couponService;
        this.userService = userService;
        this.analyticsService = analyticsService;
        this.cloudinary = cloudinary;
    }

    // ==========================
    // PRODUCTS
    // ==========================

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest req) {
        return ResponseEntity.ok(productService.create(req));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest req) {

        return ResponseEntity.ok(productService.update(id, req));
    }

    @PatchMapping("/products/{id}/stock")
    public ResponseEntity<?> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {

        return ResponseEntity.ok(
                productService.updateStock(id, request.getStockQty())
        );
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        productService.delete(id);

        return ResponseEntity.ok(
                Map.of("message", "Product deleted"));
    }

    // ==========================
    // IMAGE UPLOAD
    // ==========================

    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file)
            throws Exception {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "No file selected"));
        }

        if (file.getContentType() == null ||
                !file.getContentType().startsWith("image/")) {

            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Only image files are allowed"));
        }

        var result = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.emptyMap()
        );

        return ResponseEntity.ok(
                Map.of("url", result.get("secure_url"))
        );
    }

    // ==========================
    // ORDERS
    // ==========================

    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        return ResponseEntity.ok(
                orderService.updateStatus(id, body.get("status"))
        );
    }

    // ==========================
    // COUPONS
    // ==========================

    @GetMapping("/coupons")
    public ResponseEntity<?> getCoupons() {
        return ResponseEntity.ok(couponService.getAll());
    }

    @PostMapping("/coupons")
    public ResponseEntity<?> createCoupon(
            @RequestBody Coupon coupon) {

        return ResponseEntity.ok(couponService.create(coupon));
    }

    @DeleteMapping("/coupons/{id}")
    public ResponseEntity<?> deleteCoupon(
            @PathVariable Long id) {

        couponService.delete(id);

        return ResponseEntity.ok(
                Map.of("message", "Coupon deleted"));
    }

    // ==========================
    // USERS
    // ==========================

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserDetails(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userService.getUserDetails(id)
        );
    }
    @GetMapping("/analytics")
    public ResponseEntity<?> analytics() {
        return ResponseEntity.ok(
                analyticsService.getDashboardAnalytics()
        );
    }
}