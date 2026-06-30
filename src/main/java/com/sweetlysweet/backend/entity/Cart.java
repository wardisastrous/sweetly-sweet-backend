package com.sweetlysweet.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity = 1;

    public Cart() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id =~ id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Cart c = new Cart();
        public Builder user(User v)       { c.user = v; return this; }
        public Builder product(Product v) { c.product = v; return this; }
        public Builder quantity(Integer v){ c.quantity = v; return this; }
        public Cart build()               { return c; }
    }
}