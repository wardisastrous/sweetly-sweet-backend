package com.sweetlysweet.backend.repository;

import com.sweetlysweet.backend.entity.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByActiveTrue(Pageable pageable);
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);
    Optional<Product> findByIdAndActiveTrue(Long id);
}