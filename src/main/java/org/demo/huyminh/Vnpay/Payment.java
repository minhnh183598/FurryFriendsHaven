package org.demo.huyminh.Vnpay;

import jakarta.persistence.*;
import lombok.Data;
import org.demo.huyminh.Entity.User;

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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
