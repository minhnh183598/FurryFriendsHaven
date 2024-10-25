package org.demo.huyminh.Vnpay;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String amount;
    private String bankCode;
    private String bankTranNo;
    private String cardType;
    private String orderInfo;
    private String payDate;
    private String responseCode;
    private String transactionNo;
    private String txnRef;
    private String secureHash;
    private String userId;

    public void setAmount(String amount) {
        double amountValue = Double.parseDouble(amount);
        amountValue /= 100;
        this.amount = Double.toString(amountValue);
    }
}