package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.entity.Coupon;
import com.sweetlysweet.backend.repository.CouponRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Map<String, Object> apply(String code, BigDecimal orderTotal) {
        Coupon coupon = couponRepository.findByCodeAndIsActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("Invalid or expired coupon"));

        if (coupon.getExpiry() != null && coupon.getExpiry().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Coupon has expired");

        if (coupon.getUsedCount() >= coupon.getMaxUses())
            throw new RuntimeException("Coupon usage limit reached");

        if (orderTotal.compareTo(coupon.getMinOrder()) < 0)
            throw new RuntimeException("Minimum order ₹" + coupon.getMinOrder() + " required");

        BigDecimal discount = coupon.getDiscountType().equals("PERCENT")
                ? orderTotal.multiply(coupon.getDiscountValue())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                : coupon.getDiscountValue();

        return Map.of("discountAmount", discount.min(orderTotal), "couponCode", code);
    }

    public Coupon create(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public void delete(Long id) {
        couponRepository.deleteById(id);
    }

    public List<Coupon> getAll() {
        return couponRepository.findAll();
    }
}