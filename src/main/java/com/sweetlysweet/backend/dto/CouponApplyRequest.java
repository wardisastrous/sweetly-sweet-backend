package com.sweetlysweet.backend.dto;

import java.math.BigDecimal;

public class CouponApplyRequest {
    private String code;
    private BigDecimal orderTotal;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public BigDecimal getOrderTotal() { return orderTotal; }
    public void setOrderTotal(BigDecimal orderTotal) { this.orderTotal = orderTotal; }
}