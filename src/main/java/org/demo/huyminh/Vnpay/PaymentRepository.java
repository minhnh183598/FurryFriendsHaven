package org.demo.huyminh.Vnpay;

import org.demo.huyminh.Entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Phương thức tìm payments theo tên người dùng
    List<Payment> findByUserUsername(String username);
}