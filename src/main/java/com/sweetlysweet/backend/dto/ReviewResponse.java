package com.sweetlysweet.backend.dto;

import java.time.LocalDateTime;

public class ReviewResponse {

    private Long id;
    private Long userId;
    private String userName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private boolean editable;

    public ReviewResponse() {
    }

    public ReviewResponse(
            Long id,
            Long userId,
            String userName,
            Integer rating,
            String comment,
            LocalDateTime createdAt,
            boolean editable
    ) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
        this.editable = editable;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isEditable() {
        return editable;
    }
}