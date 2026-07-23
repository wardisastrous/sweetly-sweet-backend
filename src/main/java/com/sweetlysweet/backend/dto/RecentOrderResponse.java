package com.sweetlysweet.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RecentOrderResponse {

    private Long id;
    private BigDecimal totalAmount;
    private String orderStatus;
    private LocalDateTime createdAt;

    public RecentOrderResponse() {
    }

    public RecentOrderResponse(
            Long id,
            BigDecimal totalAmount,
            String orderStatus,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}