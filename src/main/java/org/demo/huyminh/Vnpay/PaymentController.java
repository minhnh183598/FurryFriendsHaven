package org.demo.huyminh.Vnpay;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.demo.huyminh.Core.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

//Ngân hàng:         NCB
//Số thẻ:              9704198526191432198
//Tên chủ thẻ:           NGUYEN VAN A
//Ngày phát hành:          07/15
//Mật khẩu OTP:              123456

@RestController
@RequestMapping("${spring.application.api-prefix}/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }


    @GetMapping("/vn-pay-callback")
    public void payCallbackHandler(
            @RequestParam String vnp_Amount,     @RequestParam String vnp_BankCode,
            @RequestParam String vnp_BankTranNo,    @RequestParam String vnp_CardType,   @RequestParam String vnp_ResponseCode,
            @RequestParam String vnp_OrderInfo,     @RequestParam String vnp_PayDate,    @RequestParam String vnp_TxnRef,
            @RequestParam String vnp_TransactionNo, @RequestParam String vnp_SecureHash, HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String status = request.getParameter("vnp_ResponseCode");
        if ("00".equals(status)) {
            Payment payment = new Payment();
            payment.setAmount(request.getParameter("vnp_Amount"));
            payment.setBankCode(request.getParameter("vnp_BankCode"));
            payment.setBankTranNo(request.getParameter("vnp_BankTranNo"));
            payment.setCardType(request.getParameter("vnp_CardType"));
            payment.setOrderInfo(request.getParameter("vnp_OrderInfo"));
            payment.setPayDate(request.getParameter("vnp_PayDate"));
            payment.setResponseCode(status);
            payment.setTransactionNo(request.getParameter("vnp_TransactionNo"));
            payment.setTxnRef(request.getParameter("vnp_TxnRef"));
            payment.setSecureHash(request.getParameter("vnp_SecureHash"));
            String orderInfo = request.getParameter("vnp_OrderInfo");
            String userId = extractUserIdFromOrderInfo(orderInfo);
            payment.setUserId(userId);

            paymentService.savePayment(payment);
            response.sendRedirect("http://localhost:3000/thanks");

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed");
        }
    }

    private String extractUserIdFromOrderInfo(String orderInfo) {
        if (orderInfo != null && orderInfo.contains("User Id:")) {
            return orderInfo.substring(orderInfo.indexOf("User Id:") + 9).trim();
        }
        return null;
    }

    @GetMapping("/{userId}")
    public DonationResponse getDonationsByUserId(@PathVariable String userId) {
        List<Payment> donations = paymentRepository.findByUserId(userId);
        double totalAmount = 0.0;
        for (Payment donation : donations) {
            double amount = Double.parseDouble(donation.getAmount());
            totalAmount += amount;
        }
        return new DonationResponse(userId, donations, totalAmount);
    }

    @GetMapping("/all")
    public AllDonation getAllDonation(){
        List<Payment> donations = paymentRepository.findAll();
        double totalDonation = 0.0;
        for (Payment donation : donations) {
            double amount = Double.parseDouble(donation.getAmount());
            totalDonation += amount;
        }
        return new AllDonation(donations,totalDonation);
    }

}