package org.demo.huyminh.Vnpay;

import java.util.List;

public class AllDonation {
    private List<Payment> donations;
    private double totalAmount;

    public AllDonation(List<Payment> donations, double totalAmount) {
        this.donations = donations;
        this.totalAmount = totalAmount;
    }

    public List<Payment> getDonations() {
        return donations;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}
