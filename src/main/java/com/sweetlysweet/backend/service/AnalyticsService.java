package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.dto.AdminAnalyticsResponse;
import com.sweetlysweet.backend.entity.Order;
import com.sweetlysweet.backend.entity.OrderItem;
import com.sweetlysweet.backend.repository.OrderItemRepository;
import com.sweetlysweet.backend.repository.OrderRepository;
import com.sweetlysweet.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public AnalyticsService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            UserRepository userRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    public AdminAnalyticsResponse getDashboardAnalytics() {

        AdminAnalyticsResponse response = new AdminAnalyticsResponse();

        List<Order> orders = orderRepository.findAll();
        List<OrderItem> orderItems = orderItemRepository.findAll();

        BigDecimal totalRevenue = orders.stream()
                .filter(o -> "PAID".equalsIgnoreCase(o.getPaymentStatus()))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalRevenue(totalRevenue);

        response.setTotalOrders(orders.size());

        response.setTotalCustomers(userRepository.count());

        long productsSold = orderItems.stream()
                .mapToLong(OrderItem::getQuantity)
                .sum();

        response.setProductsSold(productsSold);

        if (!orders.isEmpty()) {
            response.setAverageOrderValue(
                    totalRevenue.divide(
                            BigDecimal.valueOf(orders.size()),
                            2,
                            RoundingMode.HALF_UP
                    )
            );
        } else {
            response.setAverageOrderValue(BigDecimal.ZERO);
        }

        Map<String, Long> statusCounts =
                orders.stream()
                        .collect(Collectors.groupingBy(
                                Order::getOrderStatus,
                                Collectors.counting()
                        ));

        response.setOrderStatusCounts(statusCounts);

        Map<Integer, BigDecimal> revenueMap = new TreeMap<>();

        for (Order order : orders) {

            if (!"PAID".equalsIgnoreCase(order.getPaymentStatus()))
                continue;

            int month = order.getCreatedAt().getMonthValue();

            revenueMap.merge(
                    month,
                    order.getTotalAmount(),
                    BigDecimal::add
            );
        }

        List<AdminAnalyticsResponse.MonthlyRevenue> monthly = new ArrayList<>();

        for (Map.Entry<Integer, BigDecimal> entry : revenueMap.entrySet()) {

            monthly.add(
                    new AdminAnalyticsResponse.MonthlyRevenue(
                            Month.of(entry.getKey())
                                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                            entry.getValue()
                    )
            );
        }

        response.setMonthlyRevenue(monthly);

        Map<String, Long> topProducts =
                orderItems.stream()
                        .collect(Collectors.groupingBy(
                                OrderItem::getProductName,
                                Collectors.summingLong(OrderItem::getQuantity)
                        ));

        List<AdminAnalyticsResponse.TopProduct> top =
                topProducts.entrySet()
                        .stream()
                        .sorted((a, b) ->
                                Long.compare(
                                        b.getValue(),
                                        a.getValue()
                                )
                        )
                        .limit(5)
                        .map(e ->
                                new AdminAnalyticsResponse.TopProduct(
                                        e.getKey(),
                                        e.getValue()
                                )
                        )
                        .toList();

        response.setTopProducts(top);

        return response;
    }
}