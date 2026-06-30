package com.sweetlysweet.backend.dto;

public class AuthResponse {
    private String token;
    private UserDto user;

    public AuthResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public UserDto getUser() { return user; }

    public static class UserDto {
        private Long id;
        private String name;
        private String email;
        private String phone;
        private String role;

        public UserDto(Long id, String name, String email, String phone, String role) {
            this.id = id; this.name = name; this.email = email;
            this.phone = phone; this.role = role;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getRole() { return role; }
    }
}