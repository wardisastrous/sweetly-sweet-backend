package com.sweetlysweet.backend.service;

import com.razorpay.RazorpayClient;
import com.sweetlysweet.backend.dto.CheckoutRequest;
import com.sweetlysweet.backend.entity.*;
import com.sweetlysweet.backend.repository.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository  orderRepository;
    private final CartRepository   cartRepository;
    private final UserRepository   userRepository;
    private final CouponRepository couponRepository;
    private final CouponService    couponService;
    private final EmailService     emailService;

    @Value("${razorpay.key-id}")     private String razorpayKeyId;
    @Value("${razorpay.key-secret}") private String razorpayKeySecret;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository,
                        CouponRepository couponRepository,
                        CouponService couponService,
                        EmailService emailService) {
        this.orderRepository  = orderRepository;
        this.cartRepository   = cartRepository;
        this.userRepository   = userRepository;
        this.couponRepository = couponRepository;
        this.couponService    = couponService;
        this.emailService     = emailService;
    }

    @Transactional
    public Map<String, Object> checkout(String email, CheckoutRequest req) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        BigDecimal subtotal = cartItems.stream()
                .map(c -> getPrice(c).multiply(BigDecimal.valueOf(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal discount   = BigDecimal.ZERO;
        String     couponCode = req.getCouponCode();

        if (couponCode != null && !couponCode.isBlank()) {
            try {
                discount = (BigDecimal) couponService.apply(couponCode, subtotal)
                        .get("discountAmount");
            } catch (Exception e) {
                couponCode = null;
            }
        }

        BigDecimal delivery = subtotal.compareTo(new BigDecimal("499")) >= 0
                ? BigDecimal.ZERO : new BigDecimal("49");
        BigDecimal total = subtotal.subtract(discount).add(delivery).max(BigDecimal.ZERO);

        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
        JSONObject rzpReq = new JSONObject();
        rzpReq.put("amount", total.multiply(BigDecimal.valueOf(100)).intValue());
        rzpReq.put("currency", "INR");
        rzpReq.put("receipt", "rcpt_" + System.currentTimeMillis());
        com.razorpay.Order rzpOrder = razorpay.orders.create(rzpReq);

        CheckoutRequest.AddressDto addr = req.getAddress();

        com.sweetlysweet.backend.entity.Order order =
                com.sweetlysweet.backend.entity.Order.builder()
                        .user(user)
                        .fullName(addr.getFullName())
                        .phone(addr.getPhone())
                        .street(addr.getStreet())
                        .city(addr.getCity())
                        .state(addr.getState())
                        .pincode(addr.getPincode())
                        .couponCode(couponCode)
                        .subtotal(subtotal)
                        .discountAmount(discount)
                        .deliveryCharge(delivery)
                        .totalAmount(total)
                        .razorpayOrderId(rzpOrder.get("id"))
                        .paymentStatus("PENDING")
                        .orderStatus("PENDING")
                        .build();

        final String finalCoupon = couponCode;
        List<OrderItem> items = cartItems.stream().map(c ->
                OrderItem.builder()
                        .order(order)
                        .product(c.getProduct())
                        .productName(c.getProduct().getName())
                        .priceAtPurchase(getPrice(c))
                        .quantity(c.getQuantity())
                        .build()
        ).toList();

        order.setItems(items);
        orderRepository.save(order);

        if (finalCoupon != null) {
            couponRepository.findByCodeAndIsActiveTrue(finalCoupon).ifPresent(c -> {
                c.setUsedCount(c.getUsedCount() + 1);
                couponRepository.save(c);
            });
        }

        return Map.of(
                "orderId",         order.getId(),
                "razorpayOrderId", rzpOrder.get("id").toString(),
                "razorpayAmount",  total.multiply(BigDecimal.valueOf(100)).intValue()
        );
    }

    @Transactional
    public void verifyPayment(Long orderId, String rzpOrderId,
                              String rzpPaymentId, String rzpSignature) throws Exception {
        String payload = rzpOrderId + "|" + rzpPaymentId;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(razorpayKeySecret.getBytes(), "HmacSHA256"));
        byte[] hash = mac.doFinal(payload.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));

        if (!sb.toString().equals(rzpSignature))
            throw new RuntimeException("Payment verification failed");

        com.sweetlysweet.backend.entity.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentId(rzpPaymentId);
        order.setPaymentStatus("PAID");
        order.setOrderStatus("CONFIRMED");
        orderRepository.save(order);

        cartRepository.deleteByUserId(order.getUser().getId());
        emailService.sendOrderConfirmation(order, order.getUser().getEmail());
    }

    public List<com.sweetlysweet.backend.entity.Order> getMyOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
    }

    public com.sweetlysweet.backend.entity.Order getOrderById(Long id, String email) {
        com.sweetlysweet.backend.entity.Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (!order.getUser().getEmail().equals(email))
            throw new RuntimeException("Unauthorized");
        return order;
    }

    public com.sweetlysweet.backend.entity.Order updateStatus(Long orderId, String status) {
        com.sweetlysweet.backend.entity.Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setOrderStatus(status);
        orderRepository.save(order);
        emailService.sendStatusUpdate(order, order.getUser().getEmail());
        return order;
    }

    public List<com.sweetlysweet.backend.entity.Order> getAllOrders() {
        return orderRepository.findAll(Sort.by("createdAt").descending());
    }

    private BigDecimal getPrice(Cart c) {
        return c.getProduct().getSalePrice() != null
                ? c.getProduct().getSalePrice()
                : c.getProduct().getPrice();
    }
}