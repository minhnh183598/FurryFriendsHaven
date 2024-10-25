package org.demo.huyminh.Vnpay;

import java.util.List;

public class DonationResponse {
    private String userId;
    private List<Payment> donations;
    private double totalAmount;

    public DonationResponse(String userId, List<Payment> donations, double totalAmount) {
        this.userId = userId;
        this.donations = donations;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public List<Payment> getDonations() {
        return donations;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
