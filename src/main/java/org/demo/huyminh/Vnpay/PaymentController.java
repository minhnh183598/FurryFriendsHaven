package org.demo.huyminh.Vnpay;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.demo.huyminh.Core.response.ResponseObject;
import org.demo.huyminh.DTO.Reponse.ApiResponse;
import org.demo.huyminh.DTO.Reponse.PaymentResponse;
import org.demo.huyminh.Entity.User;
import org.demo.huyminh.Exception.AppException;
import org.demo.huyminh.Exception.ErrorCode;
import org.demo.huyminh.Mapper.UserMapper;
import org.demo.huyminh.Repository.PetRepository;
import org.demo.huyminh.Repository.UserRepository;
import org.demo.huyminh.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

import static org.springframework.data.projection.EntityProjection.ProjectionType.DTO;

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
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/vn-pay")
    public ResponseObject<PaymentDTO.VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<PaymentResponse> payCallbackHandler(
            HttpServletRequest request,
            @RequestParam("userId") String userId
    ) throws IOException {
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
            User existingUser = userRepository.findById(request.getParameter("userId"))
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            payment.setUserId(userId);



            paymentService.savePayment(payment);
            PaymentResponse response = PaymentResponse.builder()
                    .status("success")
                    .payDate(request.getParameter("vnp_PayDate"))
                    .bankTranId(request.getParameter("vnp_BankTranNo"))
                    .amount(request.getParameter("vnp_Amount"))
                    .orderInfo(request.getParameter("vnp_OrderInfo"))
                    .user(userMapper.toUserResponse(existingUser))
                    .build();

            if(!request.getParameter("vnp_OrderInfo").equalsIgnoreCase("Donation for Center ")) {
                String petId = extractPetId(request.getParameter("vnp_OrderInfo"));
                response.setPet(petRepository.findById(petId).orElseThrow(() -> new AppException(ErrorCode.PET_NOT_EXISTS)));
            }

            return ApiResponse.<PaymentResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message("Get Payment Detail Successfully")
                    .result(response)
                    .build();

        } else {
            return ApiResponse.<PaymentResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message("Get Payment Detail Failed")
                    .build();
        }
    }

    private String extractPetId(String orderInfo) {
        int startIndex = orderInfo.indexOf("Id: ") + 4;

        int endIndex = orderInfo.indexOf(" ", startIndex);

        if (endIndex == -1) {
            endIndex = orderInfo.length();
        }

        return orderInfo.substring(startIndex, endIndex);
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