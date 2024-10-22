package org.demo.huyminh.Controller;

import org.demo.huyminh.Core.response.ResponseObject;
import org.demo.huyminh.Service.DonationService;
import org.demo.huyminh.Vnpay.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${spring.application.api-prefix}/donations")
public class DonationController {

    @Autowired
    private DonationService donationService;

    // API lấy tất cả lịch sử donation từ dữ liệu payment
    @GetMapping("/history")
    public ResponseObject<List<Payment>> getAllDonationHistory(
            @RequestParam(required = false) String sortBy, // Tham số để sort
            @RequestParam(required = false) String order) { // Tham số để chọn sắp xếp tăng dần hay giảm dần

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Payment> donations;

        // Kiểm tra vai trò của người dùng
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            // Nếu là admin, lấy tất cả donations
            donations = donationService.getAllPayments();
        } else {
            // Nếu là user, chỉ lấy donations của user hiện tại
            donations = donationService.getPaymentsByUser(username);
        }

        // Nếu admin và user muốn sort, xử lý việc sắp xếp
        if ("amount".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(order)) {
                donations = donations.stream()
                        .sorted(Comparator.comparing(Payment::getAmount).reversed())
                        .collect(Collectors.toList());
            } else {
                donations = donations.stream()
                        .sorted(Comparator.comparing(Payment::getAmount))
                        .collect(Collectors.toList());
            }
        } else if ("date".equalsIgnoreCase(sortBy)) {
            if ("desc".equalsIgnoreCase(order)) {
                donations = donations.stream()
                        .sorted(Comparator.comparing(Payment::getPayDate).reversed())
                        .collect(Collectors.toList());
            } else {
                donations = donations.stream()
                        .sorted(Comparator.comparing(Payment::getPayDate))
                        .collect(Collectors.toList());
            }
        }

        return new ResponseObject<>(HttpStatus.OK, "Success", donations);
    }

    // API tính tổng số tiền donate (chỉ dành cho admin)
    @GetMapping("/total")
    public ResponseObject<BigDecimal> getTotalDonations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra vai trò của người dùng
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            BigDecimal totalDonations = donationService.getAllPayments().stream()
                    .map(payment -> new BigDecimal(payment.getAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            return new ResponseObject<>(HttpStatus.OK, "Success", totalDonations);
        } else {
            return new ResponseObject<>(HttpStatus.FORBIDDEN, "Access Denied", null);
        }
    }
}
