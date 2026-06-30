package com.sweetlysweet.backend.dto;

public class CheckoutRequest {
    private AddressDto address;
    private String couponCode;

    public AddressDto getAddress() { return address; }
    public void setAddress(AddressDto address) { this.address = address; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public static class AddressDto {
        private String fullName;
        private String phone;
        private String street;
        private String city;
        private String state;
        private String pincode;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getState() { return state; }
        public void setState(String state) { this.state = state; }
        public String getPincode() { return pincode; }
        public void setPincode(String pincode) { this.pincode = pincode; }
    }
}