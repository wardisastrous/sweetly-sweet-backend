package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.dto.ProductRequest;
import com.sweetlysweet.backend.entity.Product;
import com.sweetlysweet.backend.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> getAll(String category, String sort, int limit) {
        Pageable pageable = PageRequest.of(0, limit, getSort(sort));

        if (category != null && !category.isBlank()) {
            return productRepository.findByCategoryAndActiveTrue(category, pageable);
        }

        return productRepository.findByActiveTrue(pageable);
    }

    public Product getById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product create(ProductRequest req) {
        return productRepository.save(Product.builder()
                .name(req.getName())
                .description(req.getDescription())
                .price(req.getPrice())
                .salePrice(req.getSalePrice())
                .stockQty(req.getStockQty())
                .category(req.getCategory())
                .imageUrls(req.getImageUrls())
                .active(true)
                .build());
    }

    public Product update(Long id, ProductRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setName(req.getName());
        p.setDescription(req.getDescription());
        p.setPrice(req.getPrice());
        p.setSalePrice(req.getSalePrice());
        p.setStockQty(req.getStockQty());
        p.setCategory(req.getCategory());

        if (req.getImageUrls() != null) {
            p.setImageUrls(req.getImageUrls());
        }

        return productRepository.save(p);
    }

    // NEW
    public Product updateStock(Long id, Integer stockQty) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setStockQty(stockQty);

        return productRepository.save(product);
    }

    public void delete(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setActive(false);

        productRepository.save(p);
    }

    private Sort getSort(String sort) {
        return switch (sort == null ? "newest" : sort) {
            case "price_asc" -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "popular" -> Sort.by("avgRating").descending();
            default -> Sort.by("createdAt").descending();
        };
    }
}