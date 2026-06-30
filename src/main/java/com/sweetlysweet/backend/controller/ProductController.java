package com.sweetlysweet.backend.controller;

import com.sweetlysweet.backend.repository.ReviewRepository;
import com.sweetlysweet.backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService   productService;
    private final ReviewRepository reviewRepository;

    public ProductController(ProductService productService,
                             ReviewRepository reviewRepository) {
        this.productService   = productService;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(productService.getAll(category, sort, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewRepository.findByProductId(id));
    }
}