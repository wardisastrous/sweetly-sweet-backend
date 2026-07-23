package com.sweetlysweet.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UserDetailResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private LocalDateTime createdAt;

    private String street;
    private String city;
    private String state;
    private String pincode;

    private long totalOrders;
    private double totalSpent;

    private List<RecentOrderResponse> recentOrders;

    public UserDetailResponse() {
    }

    public UserDetailResponse(
            Long id,
            String name,
            String email,
            String phone,
            String role,
            LocalDateTime createdAt,
            String street,
            String city,
            String state,
            String pincode,
            long totalOrders,
            double totalSpent,
            List<RecentOrderResponse> recentOrders
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.createdAt = createdAt;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
        this.recentOrders = recentOrders;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public List<RecentOrderResponse> getRecentOrders() {
        return recentOrders;
    }
}