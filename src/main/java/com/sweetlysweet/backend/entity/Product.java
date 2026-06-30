package com.sweetlysweet.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "sale_price")
    private BigDecimal salePrice;

    @Column(name = "sale_ends_at")
    private LocalDateTime saleEndsAt;

    @Column(name = "stock_qty")
    private Integer stockQty = 0;

    private String category;

    @Column(name = "image_urls", columnDefinition = "TEXT[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private String[] imageUrls;

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "avg_rating")
    private BigDecimal avgRating = BigDecimal.ZERO;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Product() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public LocalDateTime getSaleEndsAt() { return saleEndsAt; }
    public void setSaleEndsAt(LocalDateTime saleEndsAt) { this.saleEndsAt = saleEndsAt; }
    public Integer getStockQty() { return stockQty; }
    public void setStockQty(Integer stockQty) { this.stockQty = stockQty; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String[] getImageUrls() { return imageUrls; }
    public void setImageUrls(String[] imageUrls) { this.imageUrls = imageUrls; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public BigDecimal getAvgRating() { return avgRating; }
    public void setAvgRating(BigDecimal avgRating) { this.avgRating = avgRating; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Product p = new Product();
        public Builder name(String v)          { p.name = v; return this; }
        public Builder description(String v)   { p.description = v; return this; }
        public Builder price(BigDecimal v)     { p.price = v; return this; }
        public Builder salePrice(BigDecimal v) { p.salePrice = v; return this; }
        public Builder stockQty(Integer v)     { p.stockQty = v; return this; }
        public Builder category(String v)      { p.category = v; return this; }
        public Builder imageUrls(String[] v)   { p.imageUrls = v; return this; }
        public Builder active(Boolean v)       { p.active = v; return this; }
        public Product build()                 { return p; }
    }
}