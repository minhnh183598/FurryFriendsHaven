package org.demo.huyminh.Service;

import org.demo.huyminh.Entity.Donation;
import org.demo.huyminh.Repository.DonationRepository;
import org.demo.huyminh.Vnpay.Payment;
import org.demo.huyminh.Vnpay.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationServiceImpl implements DonationService{

    @Autowired
    private PaymentRepository paymentRepository; // Sử dụng PaymentRepository để lấy dữ liệu thanh toán



    public List<Payment> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        System.out.println("All Payments: " + payments); // Thêm log
        return payments;
    }

    public List<Payment> getPaymentsByUser(String username) {
        List<Payment> payments = paymentRepository.findByUserUsername(username);
        System.out.println("Payments for user " + username + ": " + payments); // Thêm log
        return payments;
    }

    // Phương thức tính tổng số tiền donate
    public BigDecimal getTotalDonations() {
        List<Payment> payments = paymentRepository.findAll();
        BigDecimal total = payments.stream()
                .map(payment -> new BigDecimal(payment.getAmount())) // Chuyển String amount thành BigDecimal
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Total Donations: " + total); // Log tổng số tiền donate
        return total;
    }

}
