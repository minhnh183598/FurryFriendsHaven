package org.demo.huyminh.Service;

import org.demo.huyminh.Entity.Donation;
import org.demo.huyminh.Vnpay.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface DonationService {

    List<Payment> getAllPayments();

    List<Payment> getPaymentsByUser(String username);

    BigDecimal getTotalDonations();

//    List<Donation> getAllDonations();
//
//    BigDecimal getTotalDonations();
//
//    void saveDonation(Donation donation);
}
