package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.dto.ReviewRequest;
import com.sweetlysweet.backend.service.ProductService;
import com.sweetlysweet.backend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    public ProductController(
            ProductService productService,
            ReviewService reviewService
    ) {
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return ResponseEntity.ok(
                productService.getAll(category, sort, limit)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                productService.getById(id)
        );
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getReviews(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                reviewService.getProductReviews(id)
        );
    }

    @PostMapping("/reviews")
    public ResponseEntity<?> addReview(
            @Valid @RequestBody ReviewRequest request
    ) {
        reviewService.addReview(request);
        return ResponseEntity.ok().build();
    }
}