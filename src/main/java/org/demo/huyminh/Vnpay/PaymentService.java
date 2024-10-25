package org.demo.huyminh.Vnpay;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.demo.huyminh.Core.config.payment.VNPAYConfig;
import org.demo.huyminh.Entity.Pet;
import org.demo.huyminh.Repository.UserRepository;
import org.demo.huyminh.Service.PetService;
import org.demo.huyminh.Service.UserService;
import org.demo.huyminh.Util.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPAYConfig vnPayConfig;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PetService petService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    public PaymentDTO.VNPayResponse createVnPayPayment(HttpServletRequest request) {
        long amount = Integer.parseInt(request.getParameter("amount")) * 100L;
        String bankCode = request.getParameter("bankCode");
        String petId = request.getParameter("petId");
        Pet pet = petService.getPet(petId);
        String userId = request.getParameter("userId");

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(pet,userId);
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;

        return PaymentDTO.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }
}
