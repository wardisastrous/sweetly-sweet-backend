package com.sweetlysweet.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    private String phone;

    // NEW ADDRESS FIELDS
    private String street;

    private String city;

    private String state;

    private String pincode;

    @Column(nullable = false)
    private String role = "CUSTOMER";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // NEW GETTERS & SETTERS

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final User u = new User();

        public Builder name(String v) {
            u.name = v;
            return this;
        }

        public Builder email(String v) {
            u.email = v;
            return this;
        }

        public Builder passwordHash(String v) {
            u.passwordHash = v;
            return this;
        }

        public Builder phone(String v) {
            u.phone = v;
            return this;
        }

        public Builder street(String v) {
            u.street = v;
            return this;
        }

        public Builder city(String v) {
            u.city = v;
            return this;
        }

        public Builder state(String v) {
            u.state = v;
            return this;
        }

        public Builder pincode(String v) {
            u.pincode = v;
            return this;
        }

        public Builder role(String v) {
            u.role = v;
            return this;
        }

        public User build() {
            return u;
        }
    }
}