package com.sweetlysweet.backend.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AdminAnalyticsResponse {

    private BigDecimal totalRevenue;
    private long totalOrders;
    private long totalCustomers;
    private long productsSold;
    private BigDecimal averageOrderValue;

    private Map<String, Long> orderStatusCounts;

    private List<MonthlyRevenue> monthlyRevenue;

    private List<TopProduct> topProducts;

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public long getProductsSold() {
        return productsSold;
    }

    public void setProductsSold(long productsSold) {
        this.productsSold = productsSold;
    }

    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }

    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }

    public Map<String, Long> getOrderStatusCounts() {
        return orderStatusCounts;
    }

    public void setOrderStatusCounts(Map<String, Long> orderStatusCounts) {
        this.orderStatusCounts = orderStatusCounts;
    }

    public List<MonthlyRevenue> getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(List<MonthlyRevenue> monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public List<TopProduct> getTopProducts() {
        return topProducts;
    }

    public void setTopProducts(List<TopProduct> topProducts) {
        this.topProducts = topProducts;
    }

    public static class MonthlyRevenue {

        private String month;
        private BigDecimal revenue;

        public MonthlyRevenue() {}

        public MonthlyRevenue(String month, BigDecimal revenue) {
            this.month = month;
            this.revenue = revenue;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    public static class TopProduct {

        private String productName;
        private long quantitySold;

        public TopProduct() {}

        public TopProduct(String productName, long quantitySold) {
            this.productName = productName;
            this.quantitySold = quantitySold;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public long getQuantitySold() {
            return quantitySold;
        }

        public void setQuantitySold(long quantitySold) {
            this.quantitySold = quantitySold;
        }
    }
}