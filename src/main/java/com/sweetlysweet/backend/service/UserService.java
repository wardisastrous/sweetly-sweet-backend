package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.dto.RecentOrderResponse;
import com.sweetlysweet.backend.dto.UserDetailResponse;
import com.sweetlysweet.backend.dto.UserResponse;
import com.sweetlysweet.backend.entity.Order;
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

    public UserService(
            UserRepository userRepository,
            OrderRepository orderRepository
    ) {
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

                    if (totalSpent == null) {
                        totalSpent = BigDecimal.ZERO;
                    }

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

    public UserDetailResponse getUserDetails(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        long totalOrders =
                orderRepository.countByUser(user);

        BigDecimal totalSpent =
                orderRepository.getTotalSpentByUser(user);

        if (totalSpent == null) {
            totalSpent = BigDecimal.ZERO;
        }

        List<RecentOrderResponse> recentOrders =
                orderRepository
                        .findTop5ByUserOrderByCreatedAtDesc(user)
                        .stream()
                        .map(this::mapOrder)
                        .collect(Collectors.toList());

        return new UserDetailResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt(),
                user.getStreet(),
                user.getCity(),
                user.getState(),
                user.getPincode(),
                totalOrders,
                totalSpent.doubleValue(),
                recentOrders
        );
    }

    private RecentOrderResponse mapOrder(Order order) {

        return new RecentOrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getOrderStatus(),
                order.getCreatedAt()
        );
    }
}