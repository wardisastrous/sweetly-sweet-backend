package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.dto.ReviewRequest;
import com.sweetlysweet.backend.dto.ReviewResponse;
import com.sweetlysweet.backend.entity.Product;
import com.sweetlysweet.backend.entity.Review;
import com.sweetlysweet.backend.entity.User;
import com.sweetlysweet.backend.repository.OrderItemRepository;
import com.sweetlysweet.backend.repository.ProductRepository;
import com.sweetlysweet.backend.repository.ReviewRepository;
import com.sweetlysweet.backend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<ReviewResponse> getProductReviews(Long productId) {

        String email = getCurrentEmail();

        final Long currentUserId;

        if (email != null) {
            currentUserId = userRepository.findByEmail(email)
                    .map(User::getId)
                    .orElse(null);
        } else {
            currentUserId = null;
        }

        return reviewRepository
                .findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(r -> new ReviewResponse(
                        r.getId(),
                        r.getUser().getId(),
                        r.getUser().getName(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt(),
                        currentUserId != null &&
                                currentUserId.equals(r.getUser().getId())
                ))
                .toList();
    }

    public void addReview(ReviewRequest request) {

        String email = getCurrentEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Product product = productRepository
                .findById(request.getProductId())
                .orElseThrow(() ->
                        new RuntimeException("Product not found"));

        if (!orderItemRepository.hasPurchasedProduct(
                user.getId(),
                product.getId()
        )) {

            throw new RuntimeException(
                    "You can review only purchased products."
            );
        }

        if (reviewRepository.findByUserAndProduct(user, product).isPresent()) {
            throw new RuntimeException(
                    "You have already reviewed this product."
            );
        }

        Review review = new Review();

        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        updateAverageRating(product);
    }

    private void updateAverageRating(Product product) {

        List<Review> reviews =
                reviewRepository.findByProductIdOrderByCreatedAtDesc(
                        product.getId()
                );

        if (reviews.isEmpty()) {

            product.setAvgRating(BigDecimal.ZERO);

        } else {

            double avg =
                    reviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0);

            product.setAvgRating(
                    BigDecimal.valueOf(avg)
                            .setScale(1, RoundingMode.HALF_UP)
            );
        }

        productRepository.save(product);
    }

    private String getCurrentEmail() {

        Object principal =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return ((org.springframework.security.core.userdetails.User) principal)
                .getUsername();
    }
}