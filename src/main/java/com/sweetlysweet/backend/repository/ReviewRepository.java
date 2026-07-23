package com.sweetlysweet.backend.repository;

import com.sweetlysweet.backend.entity.Product;
import com.sweetlysweet.backend.entity.Review;
import com.sweetlysweet.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<Review> findAllByOrderByCreatedAtDesc();

    Optional<Review> findByUserAndProduct(User user, Product product);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    long countByProduct(Product product);
}