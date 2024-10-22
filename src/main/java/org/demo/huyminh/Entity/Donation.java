package org.demo.huyminh.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donation")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String amount;
    private String bankCode;
    private String transactionNo;
    private String payDate;
    @ManyToOne
    @JoinColumn(name = "user_id") // Trường foreign key để liên kết với User
    private User user;

    // Constructor có 6 tham số
    public Donation(Long id, String amount, String bankCode, String transactionNo, String payDate) {
        this.id = id;

        this.amount = amount;
        this.bankCode = bankCode;
        this.transactionNo = transactionNo;
        this.payDate = payDate;
    }
}
