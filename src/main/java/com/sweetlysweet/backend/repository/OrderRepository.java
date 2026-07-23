package com.sweetlysweet.backend.repository;

import com.sweetlysweet.backend.entity.Order;
import com.sweetlysweet.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUser(User user);

    @Query("""
            SELECT COALESCE(SUM(o.totalAmount), 0)
            FROM Order o
            WHERE o.user = :user
            """)
    BigDecimal getTotalSpentByUser(@Param("user") User user);
}