package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.dto.UserResponse;
import com.sweetlysweet.backend.entity.User;
import com.sweetlysweet.backend.repository.OrderRepository;
import com.sweetlysweet.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository,
                       OrderRepository orderRepository) {

        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {

                    long totalOrders =
                            orderRepository.countByUser(user);

                    BigDecimal totalSpent =
                            orderRepository.getTotalSpentByUser(user);

                    return new UserResponse(
                            user.getId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhone(),
                            user.getRole(),
                            user.getCreatedAt(),
                            totalOrders,
                            totalSpent.doubleValue()
                    );

                })
                .collect(Collectors.toList());
    }
}