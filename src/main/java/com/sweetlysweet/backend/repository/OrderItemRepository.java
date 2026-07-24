package com.sweetlysweet.backend.repository;

import com.sweetlysweet.backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
            SELECT COUNT(oi) > 0
            FROM OrderItem oi
            WHERE oi.order.user.id = :userId
              AND oi.product.id = :productId
              AND oi.order.paymentStatus = 'PAID'
            """)
    boolean hasPurchasedProduct(Long userId, Long productId);

    List<OrderItem> findAll();

    @Query("""
            SELECT SUM(oi.quantity)
            FROM OrderItem oi
            """)
    Long getTotalProductsSold();
}